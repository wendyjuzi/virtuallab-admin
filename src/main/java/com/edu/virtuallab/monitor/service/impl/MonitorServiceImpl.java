package com.edu.virtuallab.monitor.service.impl;

import com.edu.virtuallab.monitor.dto.SystemStatusDTO;
import com.edu.virtuallab.monitor.dto.SystemPerformanceDTO;
import com.edu.virtuallab.monitor.dto.UserBehaviorDTO;
import com.edu.virtuallab.monitor.mapper.ExperimentMapper;
import com.edu.virtuallab.monitor.mapper.OperationLogMapper;
import com.edu.virtuallab.monitor.service.MonitorService;
import com.edu.virtuallab.monitor.util.UserSessionCache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;

@Service
@RequiredArgsConstructor
public class MonitorServiceImpl implements MonitorService {

    private final UserSessionCache userSessionCache;
    private final ExperimentMapper experimentMapper;
    private final OperationLogMapper operationLogMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public SystemStatusDTO getSystemStatus() {
        int onlineUsers = userSessionCache.getOnlineUserCount();
        int activeExperiments = experimentMapper.countActiveExperiments();
        double systemLoad = getSystemLoadAverage();
        int todayOperations = operationLogMapper.countTodayOperations();

        return new SystemStatusDTO(onlineUsers, activeExperiments, systemLoad, todayOperations);
    }

    @Override
    public SystemPerformanceDTO getSystemPerformance() {
        SystemPerformanceDTO dto = new SystemPerformanceDTO();
        // 当前时间点
        String now = new SimpleDateFormat("HH:00").format(new Date());
        dto.setTimePoints(Collections.singletonList(now));
        // CPU
        java.lang.management.OperatingSystemMXBean osBean = java.lang.management.ManagementFactory.getOperatingSystemMXBean();
        double cpuLoad = 0;
        if (osBean instanceof com.sun.management.OperatingSystemMXBean) {
            cpuLoad = ((com.sun.management.OperatingSystemMXBean) osBean).getSystemCpuLoad();
        }
        dto.setCpuUsage(Collections.singletonList(Math.round(cpuLoad * 10000) / 100.0));
        // 内存
        double memoryUsage = 0;
        if (osBean instanceof com.sun.management.OperatingSystemMXBean) {
            com.sun.management.OperatingSystemMXBean sunOsBean = (com.sun.management.OperatingSystemMXBean) osBean;
            long totalMemory = sunOsBean.getTotalPhysicalMemorySize();
            long freeMemory = sunOsBean.getFreePhysicalMemorySize();
            if (totalMemory > 0) {
                memoryUsage = (double)(totalMemory - freeMemory) / totalMemory * 100;
            }
        }
        dto.setMemoryUsage(Collections.singletonList(Math.round(memoryUsage * 100) / 100.0));
        // 磁盘
        File root = new File("/");
        long totalDisk = root.getTotalSpace();
        long freeDisk = root.getFreeSpace();
        double diskUsage = totalDisk > 0 ? (double)(totalDisk - freeDisk) / totalDisk * 100 : 0;
        dto.setDiskUsage(Collections.singletonList(Math.round(diskUsage * 100) / 100.0));
        // Redis
        try {
            org.springframework.data.redis.connection.RedisConnection connection = stringRedisTemplate.getConnectionFactory().getConnection();
            java.util.Properties info = connection.info();
            String connectedClients = info.getProperty("connected_clients");
            String usedMemory = info.getProperty("used_memory");
            String usedMemoryHuman = info.getProperty("used_memory_human");
            String totalCommandsProcessed = info.getProperty("total_commands_processed");
            String uptimeInSeconds = info.getProperty("uptime_in_seconds");
            String keyspaceHits = info.getProperty("keyspace_hits");
            String keyspaceMisses = info.getProperty("keyspace_misses");
            // 统计所有 db 的 key 数
            int totalKeys = 0;
            for (String name : info.stringPropertyNames()) {
                if (name.startsWith("db")) {
                    String value = info.getProperty(name); // 例：keys=10,expires=0,avg_ttl=0
                    String[] parts = value.split(",");
                    for (String part : parts) {
                        if (part.startsWith("keys=")) {
                            totalKeys += Integer.parseInt(part.substring(5));
                        }
                    }
                }
            }
            dto.setRedisConnectedClients(connectedClients);
            dto.setRedisUsedMemory(usedMemory);
            dto.setRedisUsedMemoryHuman(usedMemoryHuman);
            dto.setRedisTotalCommandsProcessed(totalCommandsProcessed);
            dto.setRedisUptimeInSeconds(uptimeInSeconds);
            dto.setRedisKeyspaceHits(keyspaceHits);
            dto.setRedisKeyspaceMisses(keyspaceMisses);
            dto.setRedisTotalKeys(String.valueOf(totalKeys));
            connection.close();
        } catch (Exception e) {
            // 忽略redis异常
        }
        return dto;
    }

    @Override
    public UserBehaviorDTO getUserBehaviorAnalysis(Date startTime, Date endTime) {
        List<Map<String, Object>> raw = operationLogMapper.countOperationByHour(startTime, endTime);
        // 组装所有小时点
        Set<String> hourSet = new TreeSet<>();
        for (Map<String, Object> row : raw) {
            hourSet.add((String) row.get("hour"));
        }
        List<String> timePoints = new ArrayList<>(hourSet);
        List<Integer> viewData = new ArrayList<>();
        List<Integer> createData = new ArrayList<>();
        List<Integer> editData = new ArrayList<>();
        List<Integer> deleteData = new ArrayList<>();
        for (String hour : timePoints) {
            int view = 0, create = 0, edit = 0, del = 0;
            for (Map<String, Object> row : raw) {
                if (hour.equals(row.get("hour"))) {
                    String op = (String) row.get("operation");
                    int cnt = ((Number) row.get("cnt")).intValue();
                    if ("view".equalsIgnoreCase(op)) view = cnt;
                    else if ("create".equalsIgnoreCase(op)) create = cnt;
                    else if ("edit".equalsIgnoreCase(op)) edit = cnt;
                    else if ("delete".equalsIgnoreCase(op)) del = cnt;
                }
            }
            viewData.add(view);
            createData.add(create);
            editData.add(edit);
            deleteData.add(del);
        }
        UserBehaviorDTO dto = new UserBehaviorDTO();
        dto.setTimePoints(timePoints);
        dto.setViewData(viewData);
        dto.setCreateData(createData);
        dto.setEditData(editData);
        dto.setDeleteData(deleteData);
        return dto;
    }

    /**
     * 获取今日所有登录行为总次数
     */
    public int getTodayLoginOperationCount() {
        return operationLogMapper.countTodayLoginOperations();
    }

    @Override
    public int getTodayLoginUserCount() {
        return operationLogMapper.countTodayLoginUserCount();
    }

    private double getSystemLoadAverage() {
        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
        return osBean.getSystemLoadAverage();
    }
}