package com.edu.virtuallab.common.enums;

public enum NotificationType {
    PROJECT_SUBMITTED("project_submitted"),  // 项目提交审核
    PROJECT_APPROVED("project_approved"),  // 项目审核通过
    PROJECT_REJECTED("project_rejected"),  // 项目审核驳回
    PROJECT_PUBLISHED("project_published"),  // 项目发布
    SYSTEM("system");  // 系统通知

    private final String value;

    NotificationType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static NotificationType fromValue(String value) {
        for (NotificationType type : values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid NotificationType value: " + value);
    }
}