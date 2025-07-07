package com.edu.virtuallab.monitor.controller;

import com.edu.virtuallab.common.api.CommonResult;
import com.edu.virtuallab.log.model.OperationLog;
import com.edu.virtuallab.log.service.OperationLogService;
import com.edu.virtuallab.monitor.dto.SystemStatusDTO;
import com.edu.virtuallab.monitor.service.MonitorService;
import com.edu.virtuallab.notification.model.PageResult;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.edu.virtuallab.auth.dao.PermissionDao;
import com.edu.virtuallab.monitor.dto.PermissionStatDTO;
import com.edu.virtuallab.monitor.dto.UserBehaviorDTO;
import com.edu.virtuallab.log.dao.OperationLogDao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.edu.virtuallab.monitor.dto.SystemPerformanceDTO;
import java.util.Arrays;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@RestController
@RequestMapping("/monitor")
@RequiredArgsConstructor
public class MonitorController {

    private final MonitorService monitorService;
    private final OperationLogService operationLogService;

    @Autowired(required = false)
    private PermissionDao permissionDao;
    @Autowired(required = false)
    private OperationLogDao operationLogDao;

    /**
     * 统一系统状态接口
     */
    @GetMapping("/status")
    public CommonResult<SystemStatusDTO> getSystemStatus() {
        SystemStatusDTO statusDTO = monitorService.getSystemStatus();
        return CommonResult.success(statusDTO, "获取系统状态成功");
    }


    /**
     * 日志详情
     */
    @GetMapping("/operations/logs/{id}")
    public CommonResult<OperationLog> getOperationLogDetail(@PathVariable Long id) {
        OperationLog log = operationLogService.getLogById(id);
        return log != null ? CommonResult.success(log, "获取日志详情成功") : CommonResult.failed("获取日志详情失败");
    }

    /**
     * 权限统计接口
     */
    @GetMapping("/permission-stats")
    public CommonResult<PermissionStatDTO> getPermissionStats() {
        PermissionStatDTO dto = new PermissionStatDTO();
        // 权限总数
        int totalPermissions = 0;
        if (permissionDao != null && permissionDao.findAll() != null) {
            totalPermissions = permissionDao.findAll().size();
        }
        dto.setTotalPermissions(totalPermissions);
        // 各权限分配情况（示例：每个权限被多少角色拥有）
        // 这里只做简单统计，如需更详细可扩展
        // TODO: 可扩展为统计每个权限被多少用户/角色拥有
        dto.setPermissionDistribution(null);
        dto.setRolePermissionCount(null);
        return CommonResult.success(dto, "获取权限统计成功");
    }

    /**
     * 用户行为分析接口
     */
    @GetMapping("/user-behavior")
    public CommonResult<UserBehaviorDTO> getUserBehavior(@RequestParam(required = false) String range) {
        // 默认统计今日
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date endTime = cal.getTime();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        Date startTime = cal.getTime();
        UserBehaviorDTO dto = monitorService.getUserBehaviorAnalysis(startTime, endTime);
        return CommonResult.success(dto, "获取用户行为分析成功");
    }

    /**
     * 系统性能监控接口
     */
    @GetMapping("/performance")
    public CommonResult<SystemPerformanceDTO> getSystemPerformance() {
        SystemPerformanceDTO dto = monitorService.getSystemPerformance();
        return CommonResult.success(dto, "获取系统性能数据成功");
    }

    /**
     * 系统性能数据接口（兼容前端路径）
     */
    @GetMapping("/system-status")
    public CommonResult<SystemStatusDTO> getSystemStatusV2() {
        SystemStatusDTO statusDTO = monitorService.getSystemStatus();
        return CommonResult.success(statusDTO, "获取系统性能数据成功");
    }

    /**
     * 获取今日所有登录行为总次数
     */
    @GetMapping("/login-operation/today-count")
    public CommonResult<Integer> getTodayLoginOperationCount() {
        int count = monitorService.getTodayLoginOperationCount();
        return CommonResult.success(count, "获取今日登录行为总次数成功");
    }

    /**
     * 获取今日登录用户数（去重）
     */
    @GetMapping("/login-operation/today-user-count")
    public CommonResult<Integer> getTodayLoginUserCount() {
        int count = monitorService.getTodayLoginUserCount();
        return CommonResult.success(count, "获取今日登录用户数成功");
    }

    /**
     * 详细操作日志分页接口
     */


    /**
     * 实时操作监控接口，返回最近N条详细日志
     */
    @GetMapping("/realtime-operations")
    public CommonResult<List<OperationLog>> getRealtimeOperations(@RequestParam(defaultValue = "10") int limit) {
        List<OperationLog> logs = operationLogService.getLatestLogs(limit);
        return CommonResult.success(logs, "获取实时操作日志成功");
    }

    /**
     * 用户活跃度热力图
     */
    @GetMapping("/user-activity-heatmap")
    public CommonResult<Map<String, Object>> getUserActivityHeatmap(@RequestParam(defaultValue = "today") String range) {
        // TODO: 调用service统计24小时*7天的活跃度，返回hours, days, data
        Map<String, Object> result = new HashMap<>();
        // 示例数据，实际应从数据库统计
        result.put("hours", java.util.stream.IntStream.range(0,24).toArray());
        result.put("days", new String[]{"周一","周二","周三","周四","周五","周六","周日"});
        result.put("data", new int[][]{{0,0,10},{1,0,20}}); // [hour, day, value]
        return CommonResult.success(result, "获取用户活跃度热力图成功");
    }

    /**
     * 用户角色分布
     */
    @GetMapping("/user-role-distribution")
    public CommonResult<List<Map<String, Object>>> getUserRoleDistribution() {
        // TODO: 调用service统计用户角色分布
        List<Map<String, Object>> data = new java.util.ArrayList<>();
        Map<String, Object> m1 = new HashMap<>(); m1.put("name", "学生"); m1.put("value", 65); data.add(m1);
        Map<String, Object> m2 = new HashMap<>(); m2.put("name", "教师"); m2.put("value", 25); data.add(m2);
        Map<String, Object> m3 = new HashMap<>(); m3.put("name", "管理员"); m3.put("value", 10); data.add(m3);
        return CommonResult.success(data, "获取用户角色分布成功");
    }

    /**
     * 用户地域分布
     */
    @GetMapping("/user-region-distribution")
    public CommonResult<List<Map<String, Object>>> getUserRegionDistribution() {
        // TODO: 调用service统计用户地域分布
        List<Map<String, Object>> data = new java.util.ArrayList<>();
        Map<String, Object> m1 = new HashMap<>(); m1.put("name", "北京"); m1.put("value", 120); data.add(m1);
        Map<String, Object> m2 = new HashMap<>(); m2.put("name", "上海"); m2.put("value", 100); data.add(m2);
        return CommonResult.success(data, "获取用户地域分布成功");
    }

    /**
     * 用户学习路径（桑基图）
     */
    @GetMapping("/user-learning-path")
    public CommonResult<Map<String, Object>> getUserLearningPath() {
        // TODO: 调用service统计用户学习路径
        Map<String, Object> data = new HashMap<>();
        java.util.List<Map<String, Object>> nodes = new java.util.ArrayList<>();
        nodes.add(new HashMap<String, Object>() {{ put("name", "登录"); }});
        nodes.add(new HashMap<String, Object>() {{ put("name", "浏览实验"); }});
        nodes.add(new HashMap<String, Object>() {{ put("name", "开始实验"); }});
        nodes.add(new HashMap<String, Object>() {{ put("name", "完成实验"); }});
        nodes.add(new HashMap<String, Object>() {{ put("name", "提交报告"); }});
        nodes.add(new HashMap<String, Object>() {{ put("name", "查看成绩"); }});
        data.put("nodes", nodes);
        java.util.List<Map<String, Object>> links = new java.util.ArrayList<>();
        links.add(new HashMap<String, Object>() {{ put("source", "登录"); put("target", "浏览实验"); put("value", 100); }});
        links.add(new HashMap<String, Object>() {{ put("source", "浏览实验"); put("target", "开始实验"); put("value", 80); }});
        data.put("links", links);
        return CommonResult.success(data, "获取用户学习路径成功");
    }

    /**
     * 性能仪表盘
     */
    @GetMapping("/performance-gauges")
    public CommonResult<Map<String, Object>> getPerformanceGauges() {
        // TODO: 调用service获取cpu/memory/disk/network
        Map<String, Object> data = new HashMap<>();
        data.put("cpu", 35);
        data.put("memory", 60);
        data.put("disk", 70);
        data.put("network", 40);
        return CommonResult.success(data, "获取性能仪表盘数据成功");
    }

    /**
     * 网络流量监控
     */
    @GetMapping("/network-traffic")
    public CommonResult<Map<String, Object>> getNetworkTraffic() {
        // TODO: 调用service获取网络流量
        Map<String, Object> data = new HashMap<>();
        data.put("times", java.util.stream.IntStream.range(0,24).mapToObj(i->i+":00").toArray());
        data.put("upload", java.util.stream.IntStream.range(0,24).map(i->(int)(Math.random()*100)).toArray());
        data.put("download", java.util.stream.IntStream.range(0,24).map(i->(int)(Math.random()*200)).toArray());
        return CommonResult.success(data, "获取网络流量数据成功");
    }

    /**
     * 响应时间箱线图
     */
    @GetMapping("/response-time-boxplot")
    public CommonResult<List<List<Integer>>> getResponseTimeBoxplot() {
        // TODO: 调用service获取响应时间分布
        List<List<Integer>> data = new java.util.ArrayList<>();
        data.add(new java.util.ArrayList<>(java.util.Arrays.asList(10,20,30,40,50)));
        data.add(new java.util.ArrayList<>(java.util.Arrays.asList(15,25,35,45,55)));
        return CommonResult.success(data, "获取响应时间箱线图数据成功");
    }

    /**
     * 错误率趋势
     */
    @GetMapping("/error-rate-trend")
    public CommonResult<Map<String, Object>> getErrorRateTrend() {
        // TODO: 调用service获取错误率趋势
        Map<String, Object> data = new HashMap<>();
        data.put("times", java.util.stream.IntStream.range(0,24).mapToObj(i->i+":00").toArray());
        data.put("errorRate", java.util.stream.IntStream.range(0,24).mapToDouble(i->Math.random()*5).toArray());
        return CommonResult.success(data, "获取错误率趋势成功");
    }

    /**
     * 资源使用统计
     */
    @GetMapping("/resource-usage")
    public CommonResult<List<Map<String, Object>>> getResourceUsage() {
        // TODO: 调用service统计资源使用
        List<Map<String, Object>> data = new java.util.ArrayList<>();
        Map<String, Object> m1 = new HashMap<>(); m1.put("name", "实验文档"); m1.put("value", 45); data.add(m1);
        Map<String, Object> m2 = new HashMap<>(); m2.put("name", "视频教程"); m2.put("value", 35); data.add(m2);
        return CommonResult.success(data, "获取资源使用统计成功");
    }

    /**
     * 资源评分分布
     */
    @GetMapping("/resource-rating-histogram")
    public CommonResult<List<Integer>> getResourceRatingHistogram() {
        // TODO: 调用service统计资源评分分布
        List<Integer> data = new java.util.ArrayList<>(java.util.Arrays.asList(10,20,30,40,50,60,70,80,90));
        return CommonResult.success(data, "获取资源评分分布成功");
    }

    /**
     * 资源下载趋势
     */
    @GetMapping("/resource-download-trend")
    public CommonResult<Map<String, Object>> getResourceDownloadTrend() {
        // TODO: 调用service统计资源下载趋势
        Map<String, Object> data = new HashMap<>();
        data.put("times", new java.util.ArrayList<>(java.util.Arrays.asList("第1天","第2天","第3天","第4天","第5天","第6天","第7天")));
        data.put("downloads", new java.util.ArrayList<>(java.util.Arrays.asList(100,200,300,400,500,600,700)));
        return CommonResult.success(data, "获取资源下载趋势成功");
    }

    /**
     * 热门资源标签
     */
    @GetMapping("/resource-tags-wordcloud")
    public CommonResult<List<Map<String, Object>>> getResourceTagsWordcloud() {
        // TODO: 调用service统计热门标签
        List<Map<String, Object>> data = new java.util.ArrayList<>();
        Map<String, Object> m1 = new HashMap<>(); m1.put("name", "Java"); m1.put("value", 100); data.add(m1);
        Map<String, Object> m2 = new HashMap<>(); m2.put("name", "Python"); m2.put("value", 80); data.add(m2);
        return CommonResult.success(data, "获取热门资源标签成功");
    }

    // 后续权限使用统计、用户行为分析等接口可追加
}
