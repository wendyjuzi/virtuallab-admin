package com.edu.virtuallab.common.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum NotificationType {
    PROJECT_SUBMITTED("project_submitted"),  // 项目提交审核
    PROJECT_APPROVED("project_approved"),  // 项目审核通过
    PROJECT_REJECTED("project_rejected"),  // 项目审核驳回
    PROJECT_PUBLISHED("project_published"),  // 项目发布
    RESOURCE_SHARED("resource_shared"),  // 资源分享
    RESOURCE_SHARE_REVOKED("resource_share_revoked"),  // 资源分享撤销
    RESOURCE_SHARE_PERMISSION_UPDATED("resource_share_permission_updated"),  // 资源分享权限更新
    SYSTEM("system");  // 系统通知

    private final String value;

    NotificationType(String value) {
        this.value = value;
    }

    // 确保这个注解用于序列化
    @JsonValue
    public String getValue() {
        return value;
    }

    // 确保这个注解用于反序列化
    @JsonCreator
    public static NotificationType fromValue(String value) {
        for (NotificationType type : values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid NotificationType value: " + value);
    }
}