-- 权限管理系统数据库表结构
-- 创建时间: 2024年

-- 1. 院系表
CREATE TABLE IF NOT EXISTS `department` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(100) NOT NULL COMMENT '院系名称',
  `code` varchar(50) NOT NULL COMMENT '院系编码',
  `description` text COMMENT '院系描述',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父院系ID',
  `level` int(11) DEFAULT 1 COMMENT '院系级别：1-学院 2-系 3-专业',
  `status` int(11) DEFAULT 1 COMMENT '状态：0-禁用 1-启用',
  `contact_person` varchar(50) DEFAULT NULL COMMENT '联系人',
  `contact_phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `contact_email` varchar(100) DEFAULT NULL COMMENT '联系邮箱',
  `sort` int(11) DEFAULT 0 COMMENT '排序',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code` (`code`),
  KEY `idx_parent_id` (`parent_id`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='院系表';

-- 2. 角色表
CREATE TABLE IF NOT EXISTS `role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(100) NOT NULL COMMENT '角色名称',
  `code` varchar(50) NOT NULL COMMENT '角色编码',
  `description` text COMMENT '角色描述',
  `type` int(11) DEFAULT 1 COMMENT '角色类型：1-系统角色 2-自定义角色',
  `level` int(11) DEFAULT 1 COMMENT '权限级别：1-系统管理员 2-院系管理员 3-教师 4-学生 5-访客',
  `status` int(11) DEFAULT 1 COMMENT '状态：0-禁用 1-启用',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父角色ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code` (`code`),
  KEY `idx_type` (`type`),
  KEY `idx_level` (`level`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- 3. 权限表
CREATE TABLE IF NOT EXISTS `permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(100) NOT NULL COMMENT '权限名称',
  `code` varchar(100) NOT NULL COMMENT '权限编码',
  `type` varchar(20) DEFAULT 'permission' COMMENT '权限类型：menu-菜单 permission-权限',
  `module` varchar(50) DEFAULT NULL COMMENT '功能模块：experiment-实验管理 resource-资源库 score-成绩管理 user-用户管理 system-系统管理',
  `action` varchar(20) DEFAULT NULL COMMENT '操作类型：create-创建 view-查看 edit-编辑 delete-删除 approve-审批',
  `resource` varchar(200) DEFAULT NULL COMMENT '资源标识',
  `description` text COMMENT '权限描述',
  `status` int(11) DEFAULT 1 COMMENT '状态：0-禁用 1-启用',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父权限ID',
  `sort` int(11) DEFAULT 0 COMMENT '排序',
  `icon` varchar(100) DEFAULT NULL COMMENT '图标',
  `path` varchar(200) DEFAULT NULL COMMENT '路径',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code` (`code`),
  KEY `idx_type` (`type`),
  KEY `idx_module` (`module`),
  KEY `idx_status` (`status`),
  KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权限表';

-- 4. 用户角色关联表（扩展）
CREATE TABLE IF NOT EXISTS `user_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `department_id` bigint(20) DEFAULT NULL COMMENT '关联的院系ID',
  `status` int(11) DEFAULT 1 COMMENT '状态：0-禁用 1-启用',
  `start_time` datetime DEFAULT NULL COMMENT '权限生效时间',
  `end_time` datetime DEFAULT NULL COMMENT '权限失效时间',
  `reason` varchar(500) DEFAULT NULL COMMENT '权限分配原因',
  `created_by` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_role` (`user_id`, `role_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_role_id` (`role_id`),
  KEY `idx_department_id` (`department_id`),
  KEY `idx_status` (`status`),
  KEY `idx_end_time` (`end_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

-- 5. 角色权限关联表
CREATE TABLE IF NOT EXISTS `role_permission` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色ID',
  `permission_id` bigint(20) NOT NULL COMMENT '权限ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_role_permission` (`role_id`, `permission_id`),
  KEY `idx_role_id` (`role_id`),
  KEY `idx_permission_id` (`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色权限关联表';

-- 6. 操作日志表
CREATE TABLE IF NOT EXISTS `operation_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` bigint(20) DEFAULT NULL COMMENT '操作用户ID',
  `username` varchar(100) DEFAULT NULL COMMENT '操作用户名',
  `operation` varchar(50) DEFAULT NULL COMMENT '操作类型',
  `module` varchar(50) DEFAULT NULL COMMENT '功能模块',
  `description` varchar(500) DEFAULT NULL COMMENT '操作描述',
  `request_method` varchar(10) DEFAULT NULL COMMENT '请求方法',
  `request_url` varchar(500) DEFAULT NULL COMMENT '请求URL',
  `request_params` text COMMENT '请求参数',
  `response_result` text COMMENT '响应结果',
  `ip_address` varchar(50) DEFAULT NULL COMMENT 'IP地址',
  `user_agent` varchar(500) DEFAULT NULL COMMENT '用户代理',
  `execution_time` bigint(20) DEFAULT NULL COMMENT '执行时间（毫秒）',
  `status` int(11) DEFAULT 1 COMMENT '操作状态：0-失败 1-成功',
  `error_message` text COMMENT '错误信息',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_username` (`username`),
  KEY `idx_operation` (`operation`),
  KEY `idx_module` (`module`),
  KEY `idx_create_time` (`create_time`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';

-- 7. 权限模板表
CREATE TABLE IF NOT EXISTS `permission_template` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(100) NOT NULL COMMENT '模板名称',
  `code` varchar(50) NOT NULL COMMENT '模板编码',
  `description` text COMMENT '模板描述',
  `role_type` varchar(50) DEFAULT NULL COMMENT '适用角色类型',
  `status` int(11) DEFAULT 1 COMMENT '状态：0-禁用 1-启用',
  `created_by` varchar(100) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_code` (`code`),
  KEY `idx_role_type` (`role_type`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权限模板表';

-- 8. 权限模板详情表
CREATE TABLE IF NOT EXISTS `permission_template_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `template_id` bigint(20) NOT NULL COMMENT '模板ID',
  `permission_id` bigint(20) NOT NULL COMMENT '权限ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_template_permission` (`template_id`, `permission_id`),
  KEY `idx_template_id` (`template_id`),
  KEY `idx_permission_id` (`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权限模板详情表';

-- 9. 权限申请表
CREATE TABLE IF NOT EXISTS `permission_request` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `request_type` varchar(50) NOT NULL COMMENT '申请类型',
  `description` text COMMENT '申请描述',
  `status` varchar(20) DEFAULT 'PENDING' COMMENT '申请状态：PENDING-待审批 APPROVED-已批准 REJECTED-已拒绝',
  `applicant_id` bigint(20) NOT NULL COMMENT '申请人ID',
  `approver_id` bigint(20) DEFAULT NULL COMMENT '审批人ID',
  `approval_time` datetime DEFAULT NULL COMMENT '审批时间',
  `approval_comment` text COMMENT '审批意见',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_applicant_id` (`applicant_id`),
  KEY `idx_approver_id` (`approver_id`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权限申请表';

-- 10. 权限申请详情表
CREATE TABLE IF NOT EXISTS `permission_request_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `request_id` bigint(20) NOT NULL COMMENT '申请ID',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `permission_id` bigint(20) DEFAULT NULL COMMENT '权限ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_request_id` (`request_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_permission_id` (`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权限申请详情表';

-- 插入初始数据

-- 插入系统角色
INSERT INTO `role` (`name`, `code`, `description`, `type`, `level`, `status`) VALUES
('系统管理员', 'SYSTEM_ADMIN', '系统管理员，拥有所有权限', 1, 1, 1),
('院系管理员', 'DEPARTMENT_ADMIN', '院系管理员，管理本院系用户和资源', 1, 2, 1),
('教师', 'TEACHER', '教师，管理实验和成绩', 1, 3, 1),
('学生', 'STUDENT', '学生，参与实验和学习', 1, 4, 1),
('访客', 'GUEST', '访客，只读权限', 1, 5, 1);

-- 插入基础权限
INSERT INTO `permission` (`name`, `code`, `type`, `module`, `action`, `description`, `status`) VALUES
-- 用户管理权限
('用户查看', 'user:view', 'permission', 'user', 'view', '查看用户信息', 1),
('用户创建', 'user:create', 'permission', 'user', 'create', '创建用户', 1),
('用户编辑', 'user:edit', 'permission', 'user', 'edit', '编辑用户信息', 1),
('用户删除', 'user:delete', 'permission', 'user', 'delete', '删除用户', 1),
('用户审批', 'user:approve', 'permission', 'user', 'approve', '审批用户申请', 1),

-- 角色管理权限
('角色查看', 'role:view', 'permission', 'system', 'view', '查看角色信息', 1),
('角色创建', 'role:create', 'permission', 'system', 'create', '创建角色', 1),
('角色编辑', 'role:edit', 'permission', 'system', 'edit', '编辑角色信息', 1),
('角色删除', 'role:delete', 'permission', 'system', 'delete', '删除角色', 1),

-- 权限管理权限
('权限查看', 'permission:view', 'permission', 'system', 'view', '查看权限信息', 1),
('权限分配', 'permission:assign', 'permission', 'system', 'edit', '分配权限', 1),

-- 实验管理权限
('实验查看', 'experiment:view', 'permission', 'experiment', 'view', '查看实验信息', 1),
('实验创建', 'experiment:create', 'permission', 'experiment', 'create', '创建实验', 1),
('实验编辑', 'experiment:edit', 'permission', 'experiment', 'edit', '编辑实验信息', 1),
('实验删除', 'experiment:delete', 'permission', 'experiment', 'delete', '删除实验', 1),
('实验审批', 'experiment:approve', 'permission', 'experiment', 'approve', '审批实验', 1),

-- 资源管理权限
('资源查看', 'resource:view', 'permission', 'resource', 'view', '查看资源信息', 1),
('资源创建', 'resource:create', 'permission', 'resource', 'create', '创建资源', 1),
('资源编辑', 'resource:edit', 'permission', 'resource', 'edit', '编辑资源信息', 1),
('资源删除', 'resource:delete', 'permission', 'resource', 'delete', '删除资源', 1),
('资源审批', 'resource:approve', 'permission', 'resource', 'approve', '审批资源', 1),

-- 成绩管理权限
('成绩查看', 'score:view', 'permission', 'score', 'view', '查看成绩信息', 1),
('成绩创建', 'score:create', 'permission', 'score', 'create', '创建成绩', 1),
('成绩编辑', 'score:edit', 'permission', 'score', 'edit', '编辑成绩信息', 1),
('成绩删除', 'score:delete', 'permission', 'score', 'delete', '删除成绩', 1),
('成绩审批', 'score:approve', 'permission', 'score', 'approve', '审批成绩', 1),

-- 系统管理权限
('系统配置', 'system:config', 'permission', 'system', 'edit', '系统配置管理', 1),
('日志查看', 'system:log', 'permission', 'system', 'view', '查看系统日志', 1),
('统计查看', 'system:statistics', 'permission', 'system', 'view', '查看系统统计', 1);

-- 插入院系数据
INSERT INTO `department` (`name`, `code`, `description`, `level`, `status`) VALUES
('计算机学院', 'CS', '计算机科学与技术学院', 1, 1),
('软件工程系', 'SE', '软件工程系', 2, 1),
('网络工程系', 'NE', '网络工程系', 2, 1),
('数据科学系', 'DS', '数据科学与大数据技术系', 2, 1);

-- 为系统管理员分配所有权限
INSERT INTO `role_permission` (`role_id`, `permission_id`)
SELECT 1, id FROM `permission` WHERE status = 1;

-- 为院系管理员分配本院系管理权限
INSERT INTO `role_permission` (`role_id`, `permission_id`)
SELECT 2, id FROM `permission` WHERE module IN ('user', 'experiment', 'resource', 'score') AND action IN ('view', 'edit', 'approve');

-- 为教师分配教学相关权限
INSERT INTO `role_permission` (`role_id`, `permission_id`)
SELECT 3, id FROM `permission` WHERE module IN ('experiment', 'resource', 'score') AND action IN ('view', 'create', 'edit');

-- 为学生分配学习相关权限
INSERT INTO `role_permission` (`role_id`, `permission_id`)
SELECT 4, id FROM `permission` WHERE module IN ('experiment', 'resource') AND action = 'view';

-- 为访客分配只读权限
INSERT INTO `role_permission` (`role_id`, `permission_id`)
SELECT 5, id FROM `permission` WHERE action = 'view'; 