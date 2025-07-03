package com.edu.virtuallab.log.model;

import java.util.Date;

public class OperationLog {
    private Long id;
    private Long userId; // 操作用户ID
    private String username; // 操作用户名
    private String operation; // 操作类型
    private String module; // 功能模块
    private String action; // 具体操作
    private String target; // 操作目标
    private String description; // 操作描述
    private String requestMethod; // 请求方法
    private String requestUrl; // 请求URL
    private String requestParams; // 请求参数
    private String responseResult; // 响应结果
    private String ipAddress; // IP地址
    private String userAgent; // 用户代理
    private Long executionTime; // 执行时间（毫秒）
    private Integer status; // 操作状态：0-失败 1-成功
    private String errorMessage; // 错误信息
    private Date createTime;
    private String permissionCode;

    // 操作类型常量
    public static final String OPERATION_LOGIN = "LOGIN";
    public static final String OPERATION_LOGOUT = "LOGOUT";
    public static final String OPERATION_CREATE = "CREATE";
    public static final String OPERATION_UPDATE = "UPDATE";
    public static final String OPERATION_DELETE = "DELETE";
    public static final String OPERATION_QUERY = "QUERY";
    public static final String OPERATION_APPROVE = "APPROVE";
    public static final String OPERATION_REJECT = "REJECT";

    // 功能模块常量
    public static final String MODULE_USER = "USER";
    public static final String MODULE_ROLE = "ROLE";
    public static final String MODULE_PERMISSION = "PERMISSION";
    public static final String MODULE_DEPARTMENT = "DEPARTMENT";
    public static final String MODULE_EXPERIMENT = "EXPERIMENT";
    public static final String MODULE_RESOURCE = "RESOURCE";
    public static final String MODULE_SCORE = "SCORE";
    public static final String MODULE_SYSTEM = "SYSTEM";

    // getter/setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getOperation() { return operation; }
    public void setOperation(String operation) { this.operation = operation; }
    public String getModule() { return module; }
    public void setModule(String module) { this.module = module; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public String getTarget() { return target; }
    public void setTarget(String target) { this.target = target; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getRequestMethod() { return requestMethod; }
    public void setRequestMethod(String requestMethod) { this.requestMethod = requestMethod; }
    public String getRequestUrl() { return requestUrl; }
    public void setRequestUrl(String requestUrl) { this.requestUrl = requestUrl; }
    public String getRequestParams() { return requestParams; }
    public void setRequestParams(String requestParams) { this.requestParams = requestParams; }
    public String getResponseResult() { return responseResult; }
    public void setResponseResult(String responseResult) { this.responseResult = responseResult; }
    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
    public String getUserAgent() { return userAgent; }
    public void setUserAgent(String userAgent) { this.userAgent = userAgent; }
    public Long getExecutionTime() { return executionTime; }
    public void setExecutionTime(Long executionTime) { this.executionTime = executionTime; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
    public String getPermissionCode() {
        return permissionCode;
    }
    public void setPermissionCode(String permissionCode) {
        this.permissionCode = permissionCode;
    }
}
