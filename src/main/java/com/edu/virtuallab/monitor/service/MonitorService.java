package com.edu.virtuallab.monitor.service;

import com.edu.virtuallab.monitor.dto.SystemStatusDTO;
import com.edu.virtuallab.monitor.dto.SystemPerformanceDTO;
import com.edu.virtuallab.monitor.dto.UserBehaviorDTO;
import java.util.Date;

public interface MonitorService {
    SystemStatusDTO getSystemStatus();
    SystemPerformanceDTO getSystemPerformance();
    UserBehaviorDTO getUserBehaviorAnalysis(Date startTime, Date endTime);
    int getTodayLoginOperationCount();
    int getTodayLoginUserCount();
    // 用户活跃度热力图
    java.util.Map<String, Object> getUserActivityHeatmap(String range);
    // 用户角色分布
    java.util.List<java.util.Map<String, Object>> getUserRoleDistribution();
    // 用户地域分布
    java.util.List<java.util.Map<String, Object>> getUserRegionDistribution();
    // 用户学习路径
    java.util.Map<String, Object> getUserLearningPath();
    // 性能仪表盘
    java.util.Map<String, Object> getPerformanceGauges();
    // 网络流量
    java.util.Map<String, Object> getNetworkTraffic();
    // 响应时间箱线图
    java.util.List<java.util.List<Integer>> getResponseTimeBoxplot();
    // 错误率趋势
    java.util.Map<String, Object> getErrorRateTrend();
    // 资源使用统计
    java.util.List<java.util.Map<String, Object>> getResourceUsage();
    // 资源评分分布
    java.util.List<Integer> getResourceRatingHistogram();
    // 资源下载趋势
    java.util.Map<String, Object> getResourceDownloadTrend();
    // 热门资源标签
    java.util.List<java.util.Map<String, Object>> getResourceTagsWordcloud();
}