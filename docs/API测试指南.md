# API测试指南

## 概述

本文档提供了系统管理员和院系管理员API接口的测试指南，包括接口说明、测试用例和测试数据。

## 系统管理员API测试

### 1. 用户管理接口

#### 1.1 创建用户
```bash
POST /system-admin/users
Content-Type: application/json

{
  "username": "teacher001",
  "password": "123456",
  "realName": "张老师",
  "phone": "13800138001",
  "email": "teacher001@example.com",
  "department": "计算机学院",
  "departmentId": 1,
  "major": "计算机科学与技术",
  "grade": "2024",
  "className": "计科2401",
  "userType": "TEACHER",
  "roleIds": [3, 4]
}
```

**预期响应：**
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "username": "teacher001",
    "realName": "张老师",
    "department": "计算机学院",
    "status": 1,
    "createTime": "2024-01-01T10:00:00"
  }
}
```

#### 1.2 批量创建用户
```bash
POST /system-admin/users/batch
Content-Type: application/json

[
  {
    "username": "student001",
    "password": "123456",
    "realName": "李同学",
    "department": "计算机学院",
    "departmentId": 1,
    "userType": "STUDENT",
    "roleIds": [5]
  },
  {
    "username": "student002",
    "password": "123456",
    "realName": "王同学",
    "department": "计算机学院",
    "departmentId": 1,
    "userType": "STUDENT",
    "roleIds": [5]
  }
]
```

#### 1.3 停用用户
```bash
POST /system-admin/users/1/disable?reason=离职
```

#### 1.4 启用用户
```bash
POST /system-admin/users/1/enable
```

#### 1.5 锁定用户
```bash
POST /system-admin/users/1/lock?reason=异常登录
```

#### 1.6 解锁用户
```bash
POST /system-admin/users/1/unlock
```

#### 1.7 重置密码
```bash
POST /system-admin/users/1/reset-password?newPassword=newpass123
```

#### 1.8 删除用户
```bash
DELETE /system-admin/users/1
```

### 2. 角色权限管理接口

#### 2.1 创建角色
```bash
POST /system-admin/roles
Content-Type: application/json

{
  "name": "实验管理员",
  "code": "EXPERIMENT_ADMIN",
  "description": "负责实验项目管理",
  "type": 2,
  "level": 2,
  "status": 1
}
```

#### 2.2 为角色分配权限
```bash
POST /system-admin/roles/1/permissions
Content-Type: application/json

[1, 2, 3, 4, 5]
```

#### 2.3 为用户分配角色
```bash
POST /system-admin/users/1/roles
Content-Type: application/json

[3, 4]
```

#### 2.4 设置临时权限
```bash
POST /system-admin/users/1/temporary-permissions?roleId=3&startTime=2024-01-01 00:00:00&endTime=2024-12-31 23:59:59&reason=临时项目需要
```

### 3. 院系管理接口

#### 3.1 创建院系
```bash
POST /system-admin/departments
Content-Type: application/json

{
  "name": "信息工程学院",
  "code": "INFO_ENG",
  "description": "信息工程学院",
  "level": 1,
  "status": 1,
  "contactPerson": "张主任",
  "contactPhone": "010-12345678",
  "contactEmail": "info@example.com"
}
```

#### 3.2 创建院系管理员
```bash
POST /system-admin/department-admins
Content-Type: application/json

{
  "username": "deptadmin001",
  "password": "123456",
  "realName": "院系管理员",
  "department": "计算机学院",
  "departmentId": 1,
  "userType": "DEPARTMENT_ADMIN",
  "roleIds": [2]
}
```

### 4. 查询接口

#### 4.1 分页查询用户列表
```bash
GET /system-admin/users?username=teacher&department=计算机学院&userType=TEACHER&status=1&page=1&size=10
```

#### 4.2 获取角色列表
```bash
GET /system-admin/roles
```

#### 4.3 获取权限列表
```bash
GET /system-admin/permissions
```

#### 4.4 获取用户角色
```bash
GET /system-admin/users/1/roles
```

#### 4.5 获取角色权限
```bash
GET /system-admin/roles/1/permissions
```

### 5. 监控审计接口

#### 5.1 查询操作日志
```bash
GET /system-admin/operation-logs?username=admin&operation=CREATE&module=USER&startTime=2024-01-01&endTime=2024-12-31&page=1&size=10
```

#### 5.2 导出操作日志
```bash
GET /system-admin/operation-logs/export?username=admin&startTime=2024-01-01&endTime=2024-12-31
```

#### 5.3 获取用户登录统计
```bash
GET /system-admin/statistics/user-login?startTime=2024-01-01&endTime=2024-12-31
```

#### 5.4 获取权限使用统计
```bash
GET /system-admin/statistics/permission-usage?startTime=2024-01-01&endTime=2024-12-31
```

## 院系管理员API测试

### 1. 本院系用户管理接口

#### 1.1 创建本院系用户
```bash
POST /department-admin/users?adminUserId=2
Content-Type: application/json

{
  "username": "dept_teacher001",
  "password": "123456",
  "realName": "院系教师",
  "phone": "13800138002",
  "email": "dept_teacher001@example.com",
  "departmentId": 1,
  "major": "软件工程",
  "grade": "2024",
  "className": "软工2401",
  "userType": "TEACHER",
  "roleIds": [3]
}
```

#### 1.2 批量创建本院系用户
```bash
POST /department-admin/users/batch?adminUserId=2
Content-Type: application/json

[
  {
    "username": "dept_student001",
    "password": "123456",
    "realName": "院系学生",
    "departmentId": 1,
    "userType": "STUDENT",
    "roleIds": [5]
  }
]
```

#### 1.3 修改本院系用户
```bash
PUT /department-admin/users?adminUserId=2
Content-Type: application/json

{
  "id": 1,
  "realName": "修改后的姓名",
  "phone": "13800138003",
  "email": "newemail@example.com"
}
```

#### 1.4 停用本院系用户
```bash
POST /department-admin/users/1/disable?reason=离职&adminUserId=2
```

#### 1.5 启用本院系用户
```bash
POST /department-admin/users/1/enable?adminUserId=2
```

#### 1.6 锁定本院系用户
```bash
POST /department-admin/users/1/lock?reason=异常登录&adminUserId=2
```

#### 1.7 解锁本院系用户
```bash
POST /department-admin/users/1/unlock?adminUserId=2
```

#### 1.8 重置本院系用户密码
```bash
POST /department-admin/users/1/reset-password?newPassword=newpass123&adminUserId=2
```

### 2. 本院系权限管理接口

#### 2.1 为本院系用户分配角色
```bash
POST /department-admin/users/1/roles?adminUserId=2
Content-Type: application/json

[3, 4]
```

#### 2.2 为本院系用户设置临时权限
```bash
POST /department-admin/users/1/temporary-permissions?roleId=3&startTime=2024-01-01 00:00:00&endTime=2024-12-31 23:59:59&reason=临时项目需要&adminUserId=2
```

#### 2.3 调整本院系用户权限
```bash
POST /department-admin/users/1/permissions/adjust?adminUserId=2
Content-Type: application/json

[1, 2, 3]
```

### 3. 教学资源管理接口

#### 3.1 管理本院系教学资源
```bash
POST /department-admin/resources/1?operation=approve&adminUserId=2
```

#### 3.2 审核本院系教师提交的资源
```bash
POST /department-admin/resources/1/approve?approvalResult=approved&comment=资源质量良好&adminUserId=2
```

#### 3.3 管理本院系学生成绩数据
```bash
POST /department-admin/scores/1?operation=update&adminUserId=2
```

#### 3.4 生成本院系成绩报表
```bash
GET /department-admin/scores/report?adminUserId=2
```

### 4. 协同工作接口

#### 4.1 提交权限申请
```bash
POST /department-admin/permission-requests?requestType=ROLE_ASSIGNMENT&description=需要为教师分配实验管理员角色&adminUserId=2
Content-Type: application/json

{
  "userIds": [1, 2, 3],
  "permissionIds": [1, 2, 3]
}
```

#### 4.2 上报账号异常
```bash
POST /department-admin/user-abnormal?userId=1&abnormalType=LOGIN_FAILURE&description=连续登录失败&adminUserId=2
```

#### 4.3 获取权限申请状态
```bash
GET /department-admin/permission-requests/status?adminUserId=2
```

### 5. 查询接口

#### 5.1 分页查询本院系用户列表
```bash
GET /department-admin/users?username=teacher&realName=张&userType=TEACHER&status=1&adminUserId=2&page=1&size=10
```

#### 5.2 获取本院系用户角色
```bash
GET /department-admin/users/1/roles?adminUserId=2
```

#### 5.3 获取本院系可分配的角色列表
```bash
GET /department-admin/assignable-roles?adminUserId=2
```

#### 5.4 获取本院系可分配的权限列表
```bash
GET /department-admin/assignable-permissions?adminUserId=2
```

#### 5.5 获取本院系操作日志
```bash
GET /department-admin/operation-logs?username=teacher&operation=CREATE&module=USER&startTime=2024-01-01&endTime=2024-12-31&adminUserId=2&page=1&size=10
```

#### 5.6 获取本院系用户统计
```bash
GET /department-admin/statistics/users?adminUserId=2
```

#### 5.7 获取本院系权限使用统计
```bash
GET /department-admin/statistics/permission-usage?adminUserId=2
```

## 测试数据准备

### 1. 基础数据

#### 1.1 院系数据
```sql
INSERT INTO department (name, code, description, level, status, create_time, update_time) VALUES
('计算机学院', 'CS', '计算机科学与技术学院', 1, 1, NOW(), NOW()),
('信息工程学院', 'INFO', '信息工程学院', 1, 1, NOW(), NOW()),
('软件学院', 'SOFTWARE', '软件学院', 1, 1, NOW(), NOW());
```

#### 1.2 角色数据
```sql
INSERT INTO role (name, code, description, type, level, status, create_time, update_time) VALUES
('系统管理员', 'SYSTEM_ADMIN', '系统管理员', 1, 1, 1, NOW(), NOW()),
('院系管理员', 'DEPARTMENT_ADMIN', '院系管理员', 1, 2, 1, NOW(), NOW()),
('教师', 'TEACHER', '教师', 2, 3, 1, NOW(), NOW()),
('实验管理员', 'EXPERIMENT_ADMIN', '实验管理员', 2, 3, 1, NOW(), NOW()),
('学生', 'STUDENT', '学生', 2, 4, 1, NOW(), NOW()),
('访客', 'VISITOR', '访客', 2, 5, 1, NOW(), NOW());
```

#### 1.3 权限数据
```sql
INSERT INTO permission (name, code, description, type, status, create_time, update_time) VALUES
('用户管理', 'USER_MANAGE', '用户管理权限', 1, 1, NOW(), NOW()),
('角色管理', 'ROLE_MANAGE', '角色管理权限', 1, 1, NOW(), NOW()),
('权限管理', 'PERMISSION_MANAGE', '权限管理权限', 1, 1, NOW(), NOW()),
('院系管理', 'DEPARTMENT_MANAGE', '院系管理权限', 1, 1, NOW(), NOW()),
('教学资源管理', 'TEACH_RESOURCE_MANAGE', '教学资源管理权限', 2, 1, NOW(), NOW()),
('实验项目管理', 'EXPERIMENT_PROJECT_MANAGE', '实验项目管理权限', 2, 1, NOW(), NOW()),
('成绩管理', 'SCORE_MANAGE', '成绩管理权限', 2, 1, NOW(), NOW()),
('学生管理', 'STUDENT_MANAGE', '学生管理权限', 2, 1, NOW(), NOW());
```

### 2. 测试用户

#### 2.1 系统管理员
```sql
INSERT INTO user (username, password, real_name, department, status, create_time, update_time) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', '系统管理员', '系统', 1, NOW(), NOW());
```

#### 2.2 院系管理员
```sql
INSERT INTO user (username, password, real_name, department, status, create_time, update_time) VALUES
('deptadmin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVEFDa', '院系管理员', '计算机学院', 1, NOW(), NOW());
```

## 测试注意事项

### 1. 权限验证
- 确保测试用户具有相应的角色和权限
- 验证院系管理员只能操作本院系用户
- 验证系统管理员可以操作所有用户

### 2. 数据隔离
- 院系管理员创建的用户应该属于正确的院系
- 查询结果应该只包含本院系的数据

### 3. 事务处理
- 批量操作失败时应该回滚
- 删除操作前应该检查关联关系

### 4. 日志记录
- 所有关键操作都应该记录日志
- 日志应该包含操作人、操作时间、操作内容等信息

### 5. 异常处理
- 测试各种异常情况，如用户不存在、权限不足等
- 验证异常响应的正确性

## 性能测试

### 1. 并发测试
- 测试多用户同时操作的性能
- 测试批量操作的性能

### 2. 数据量测试
- 测试大量数据下的查询性能
- 测试分页查询的性能

### 3. 压力测试
- 测试系统在高负载下的稳定性
- 测试数据库连接池的使用情况

## 总结

通过以上测试指南，可以全面验证系统管理员和院系管理员API接口的功能正确性、性能表现和稳定性。建议按照测试用例逐一进行测试，确保系统的质量和可靠性。 