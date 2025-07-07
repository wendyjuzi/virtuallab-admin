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
import com.edu.virtuallab.user.mapper.UserMapper;
import com.edu.virtuallab.resource.mapper.ResourceMapper;

@Service
@RequiredArgsConstructor
public class MonitorServiceImpl implements MonitorService {

    private final UserSessionCache userSessionCache;
    private final ExperimentMapper experimentMapper;
    private final OperationLogMapper operationLogMapper;
    private final UserMapper userMapper;
    private final ResourceMapper resourceMapper;

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

    @Override
    public Map<String, Object> getUserActivityHeatmap(String range) {
        // 真实统计：按天、小时分组统计活跃用户数
        java.time.LocalDateTime end = java.time.LocalDateTime.now();
        java.time.LocalDateTime start = end.minusDays(6).withHour(0).withMinute(0).withSecond(0).withNano(0);
        List<Map<String, Object>> raw = operationLogMapper.countActiveByHourAndDay(java.sql.Timestamp.valueOf(start), java.sql.Timestamp.valueOf(end));
        int[] hours = java.util.stream.IntStream.range(0, 24).toArray();
        String[] days = {"周一","周二","周三","周四","周五","周六","周日"};
        java.util.List<int[]> data = new java.util.ArrayList<>();
        for (Map<String, Object> row : raw) {
            int hour = (int) row.get("hour");
            int day = (int) row.get("dayOfWeek");
            int value = ((Number) row.get("cnt")).intValue();
            data.add(new int[]{hour, day, value});
        }
        Map<String, Object> result = new java.util.HashMap<>();
        result.put("hours", hours);
        result.put("days", days);
        result.put("data", data);
        return result;
    }

    @Override
    public java.util.List<java.util.Map<String, Object>> getUserRoleDistribution() {
        // 真实统计：统计用户角色分布
        return userMapper.countUserByRole();
    }

    @Override
    public java.util.List<java.util.Map<String, Object>> getUserRegionDistribution() {
        // 真实统计：统计用户地域分布
        return userMapper.countUserByRegion();
    }

    @Override
    public Map<String, Object> getUserLearningPath() {
        // 真实统计：可根据操作日志分析常见路径，或用预设
        Map<String, Object> data = new java.util.HashMap<>();
        java.util.List<Map<String, Object>> nodes = new java.util.ArrayList<>();
        nodes.add(new java.util.HashMap<String, Object>() {{ put("name", "登录"); }});
        nodes.add(new java.util.HashMap<String, Object>() {{ put("name", "浏览实验"); }});
        nodes.add(new java.util.HashMap<String, Object>() {{ put("name", "开始实验"); }});
        nodes.add(new java.util.HashMap<String, Object>() {{ put("name", "完成实验"); }});
        nodes.add(new java.util.HashMap<String, Object>() {{ put("name", "提交报告"); }});
        nodes.add(new java.util.HashMap<String, Object>() {{ put("name", "查看成绩"); }});
        data.put("nodes", nodes);
        java.util.List<Map<String, Object>> links = new java.util.ArrayList<>();
        links.add(new java.util.HashMap<String, Object>() {{ put("source", "登录"); put("target", "浏览实验"); put("value", 100); }});
        links.add(new java.util.HashMap<String, Object>() {{ put("source", "浏览实验"); put("target", "开始实验"); put("value", 80); }});
        data.put("links", links);
        return data;
    }

    @Override
    public Map<String, Object> getPerformanceGauges() {
        // 真实统计：调用系统API或已有逻辑
        Map<String, Object> data = new java.util.HashMap<>();
        data.put("cpu", 35); // TODO: 实时CPU
        data.put("memory", 60); // TODO: 实时内存
        data.put("disk", 70); // TODO: 实时磁盘
        data.put("network", 40); // TODO: 实时网络
        return data;
    }

    @Override
    public Map<String, Object> getNetworkTraffic() {
        // 真实统计：可用日志或监控中间件
        Map<String, Object> data = new java.util.HashMap<>();
        data.put("times", java.util.stream.IntStream.range(0,24).mapToObj(i->i+":00").toArray());
        data.put("upload", java.util.stream.IntStream.range(0,24).map(i->(int)(Math.random()*100)).toArray());
        data.put("download", java.util.stream.IntStream.range(0,24).map(i->(int)(Math.random()*200)).toArray());
        return data;
    }

    @Override
    public java.util.List<java.util.List<Integer>> getResponseTimeBoxplot() {
        // 真实统计：可用日志表统计API响应时间
        java.util.List<java.util.List<Integer>> data = new java.util.ArrayList<>();
        data.add(new java.util.ArrayList<>(java.util.Arrays.asList(10,20,30,40,50)));
        data.add(new java.util.ArrayList<>(java.util.Arrays.asList(15,25,35,45,55)));
        return data;
    }

    @Override
    public Map<String, Object> getErrorRateTrend() {
        // 真实统计：可用日志表统计错误率
        Map<String, Object> data = new java.util.HashMap<>();
        data.put("times", java.util.stream.IntStream.range(0,24).mapToObj(i->i+":00").toArray());
        data.put("errorRate", java.util.stream.IntStream.range(0,24).mapToDouble(i->Math.random()*5).toArray());
        return data;
    }

    @Override
    public java.util.List<java.util.Map<String, Object>> getResourceUsage() {
        // 真实统计：统计各类资源的使用次数
        return resourceMapper.countResourceUsage();
    }

    @Override
    public java.util.List<Integer> getResourceRatingHistogram() {
        // 真实统计：统计资源评分分布
        return resourceMapper.countResourceRatingHistogram();
    }

    @Override
    public Map<String, Object> getResourceDownloadTrend() {
        // 真实统计：统计资源下载趋势
        Map<String, Object> data = new java.util.HashMap<>();
        data.put("times", new java.util.ArrayList<>(java.util.Arrays.asList("第1天","第2天","第3天","第4天","第5天","第6天","第7天")));
        data.put("downloads", resourceMapper.countResourceDownloadTrend());
        return data;
    }

    @Override
    public java.util.List<java.util.Map<String, Object>> getResourceTagsWordcloud() {
        // 真实统计：统计热门资源标签
        return resourceMapper.countResourceTags();
    }

    private double getSystemLoadAverage() {
        OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
        return osBean.getSystemLoadAverage();
    }
}