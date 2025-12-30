# DepartmentAdminService 使用指南

## 概述

`DepartmentAdminService` 是院系管理员服务接口的实现类，负责本院系范围内的权限管理功能。院系管理员只能管理自己所属院系的用户和权限。

## 主要功能模块

### 1. 本院系账号管理

#### 创建用户账号
```java
// 创建单个用户
UserCreateDTO userDTO = new UserCreateDTO();
userDTO.setUsername("teacher001");
userDTO.setPassword("123456");
userDTO.setRealName("张老师");
userDTO.setDepartment("计算机学院");
userDTO.setDepartmentId(1L);
userDTO.setUserType("TEACHER");
userDTO.setRoleIds(Arrays.asList(3L)); // 教师角色ID

User user = departmentAdminService.createDepartmentUser(userDTO, adminUserId);

// 批量创建用户
List<UserCreateDTO> userDTOs = Arrays.asList(userDTO1, userDTO2, userDTO3);
List<User> users = departmentAdminService.batchCreateDepartmentUsers(userDTOs, adminUserId);
```

#### 用户状态管理
```java
// 停用用户账号
departmentAdminService.disableDepartmentUser(userId, "离职", adminUserId);

// 启用用户账号
departmentAdminService.enableDepartmentUser(userId, adminUserId);

// 锁定用户账号
departmentAdminService.lockDepartmentUser(userId, "异常登录", adminUserId);

// 解锁用户账号
departmentAdminService.unlockDepartmentUser(userId, adminUserId);

// 重置用户密码
departmentAdminService.resetDepartmentUserPassword(userId, "newPassword123", adminUserId);
```

### 2. 本院系权限管理

#### 角色分配
```java
// 为用户分配角色
List<Long> roleIds = Arrays.asList(3L, 4L); // 教师角色、实验管理员角色
departmentAdminService.assignRolesToDepartmentUser(userId, roleIds, adminUserId);

// 移除用户角色
List<Long> roleIdsToRemove = Arrays.asList(4L);
departmentAdminService.removeDepartmentUserRoles(userId, roleIdsToRemove, adminUserId);
```

#### 临时权限管理
```java
// 设置临时权限
String startTime = "2024-01-01 00:00:00";
String endTime = "2024-12-31 23:59:59";
departmentAdminService.setDepartmentUserTemporaryPermission(
    userId, roleId, startTime, endTime, "临时项目需要", adminUserId
);

// 回收临时权限
departmentAdminService.revokeDepartmentUserTemporaryPermission(userRoleId, adminUserId);
```

### 3. 查询功能

#### 用户查询
```java
// 分页查询本院系用户
PageResult<User> userPage = departmentAdminService.getDepartmentUserList(
    "teacher", "张", "TEACHER", 1, adminUserId, 1, 10
);

// 获取用户角色
List<Role> userRoles = departmentAdminService.getDepartmentUserRoles(userId, adminUserId);
```

#### 权限查询
```java
// 获取本院系可分配的角色
List<Role> assignableRoles = departmentAdminService.getDepartmentAssignableRoles(adminUserId);

// 获取本院系可分配的权限
List<Permission> assignablePermissions = departmentAdminService.getDepartmentAssignablePermissions(adminUserId);
```

#### 操作日志查询
```java
// 查询本院系操作日志
PageResult<OperationLog> logs = departmentAdminService.getDepartmentOperationLogs(
    "teacher001", "CREATE", "USER", "2024-01-01", "2024-12-31", adminUserId, 1, 10
);
```

## 权限控制说明

### 院系隔离
- 院系管理员只能管理自己所属院系的用户
- 无法查看或操作其他院系的用户数据
- 所有操作都会验证用户所属院系

### 角色权限
- 院系管理员可以为本院系用户分配系统预设的角色
- 可以设置临时权限，但需要指定有效期
- 无法修改系统级别的权限配置

## 异常处理

### 常见异常
1. **权限不足异常**：尝试操作其他院系用户时抛出
2. **用户不存在异常**：操作的用户ID不存在时抛出
3. **角色不存在异常**：分配的角色ID不存在时抛出

### 异常处理示例
```java
try {
    departmentAdminService.createDepartmentUser(userDTO, adminUserId);
} catch (RuntimeException e) {
    if (e.getMessage().contains("无权")) {
        // 处理权限不足
        log.error("权限不足：{}", e.getMessage());
    } else {
        // 处理其他异常
        log.error("创建用户失败：{}", e.getMessage());
    }
}
```

## 数据库表依赖

### 主要表结构
- `user`：用户表
- `role`：角色表
- `permission`：权限表
- `user_role`：用户角色关联表
- `role_permission`：角色权限关联表
- `department`：院系表
- `temp_permission`：临时权限表
- `operation_log`：操作日志表

### 表关系
- 用户通过 `user_role` 表关联角色
- 角色通过 `role_permission` 表关联权限
- 用户角色关联包含院系ID，用于院系隔离
- 临时权限表记录临时授权的详细信息

## 注意事项

1. **事务管理**：所有涉及多表操作的方法都使用了 `@Transactional` 注解
2. **数据验证**：在操作前会验证用户所属院系和权限
3. **日志记录**：重要操作建议记录操作日志
4. **性能考虑**：批量操作时注意数据量大小
5. **安全性**：密码重置等敏感操作需要额外的安全验证

## 扩展功能

### 待实现功能
- 教学资源管理
- 成绩数据管理
- 权限申请流程
- 用户统计报表
- 权限使用统计

这些功能目前标记为 TODO，可以根据具体业务需求进行实现。 