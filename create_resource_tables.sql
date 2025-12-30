-- 创建资源表
CREATE TABLE IF NOT EXISTS `resource` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL COMMENT '资源名称',
  `type` varchar(50) NOT NULL COMMENT '资源类型',
  `category` varchar(100) DEFAULT NULL COMMENT '资源分类',
  `url` varchar(500) DEFAULT NULL COMMENT '资源URL',
  `status` int(11) DEFAULT 1 COMMENT '状态：1-正常，0-禁用',
  `file_size` bigint(20) DEFAULT NULL COMMENT '文件大小（字节）',
  `file_type` varchar(50) DEFAULT NULL COMMENT '文件类型',
  `uploader` varchar(100) DEFAULT NULL COMMENT '上传者',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_type` (`type`),
  KEY `idx_category` (`category`),
  KEY `idx_uploader` (`uploader`),
  KEY `idx_created_at` (`created_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资源表';

-- 创建资源点赞表
CREATE TABLE IF NOT EXISTS `resource_like` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `resource_id` bigint(20) NOT NULL COMMENT '资源ID',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_resource` (`user_id`, `resource_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_resource_id` (`resource_id`),
  KEY `idx_created_at` (`created_at`),
  CONSTRAINT `fk_resource_like_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_resource_like_resource` FOREIGN KEY (`resource_id`) REFERENCES `resource` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资源点赞表';

-- 创建资源收藏表
CREATE TABLE IF NOT EXISTS `resource_favorite` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `resource_id` bigint(20) NOT NULL COMMENT '资源ID',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_resource` (`user_id`, `resource_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_resource_id` (`resource_id`),
  KEY `idx_created_at` (`created_at`),
  CONSTRAINT `fk_resource_favorite_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_resource_favorite_resource` FOREIGN KEY (`resource_id`) REFERENCES `resource` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资源收藏表';

-- 插入一些测试数据
INSERT INTO `resource` (`name`, `type`, `category`, `url`, `status`, `file_size`, `file_type`, `uploader`) VALUES
('Java基础教程', 'document', '编程教程', 'https://example.com/java-basic.pdf', 1, 1024000, 'pdf', 'admin'),
('Python入门指南', 'document', '编程教程', 'https://example.com/python-guide.pdf', 1, 2048000, 'pdf', 'admin'),
('数据库设计原理', 'document', '数据库', 'https://example.com/database-design.pdf', 1, 1536000, 'pdf', 'teacher1'),
('Web开发实战', 'video', '前端开发', 'https://example.com/web-dev.mp4', 1, 51200000, 'mp4', 'teacher2'),
('机器学习算法', 'document', '人工智能', 'https://example.com/ml-algorithms.pdf', 1, 3072000, 'pdf', 'teacher3');

-- 插入一些测试点赞数据
INSERT INTO `resource_like` (`user_id`, `resource_id`) VALUES
(1, 1), (1, 2), (2, 1), (2, 3), (3, 1), (3, 4), (4, 2), (4, 5), (5, 1), (5, 3);

-- 插入一些测试收藏数据
INSERT INTO `resource_favorite` (`user_id`, `resource_id`) VALUES
(1, 1), (1, 3), (2, 1), (2, 4), (3, 2), (3, 5), (4, 1), (4, 3), (5, 2), (5, 4); 