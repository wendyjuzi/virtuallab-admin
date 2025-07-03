package com.edu.virtuallab.common.api;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class StatisticsDTO {
    // 系统状态
    private Integer onlineUsers;
    private Integer activeExperiments;
    private Integer systemLoad;
    private Integer todayOperations;

    // 权限统计
    @Data
    public static class PermissionStat {
        private String name;
        private String permission;
        private Integer count;
    }

    // 用户行为分析
    @Data
    public static class UserBehavior {
        private List<String> timePoints;
        private List<Integer> viewData;
        private List<Integer> createData;
        private List<Integer> editData;
        private List<Integer> deleteData;
    }

    // 系统性能
    @Data
    public static class SystemPerformance {
        private List<String> timePoints;
        private List<Integer> cpuData;
        private List<Integer> memoryData;
        private List<Integer> diskData;
    }
} 