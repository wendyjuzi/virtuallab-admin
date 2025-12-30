-- 创建资源分享表
CREATE TABLE resource_share (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    resource_id BIGINT NOT NULL COMMENT '资源ID',
    shared_by VARCHAR(50) NOT NULL COMMENT '分享者用户名',
    shared_with VARCHAR(50) COMMENT '被分享者用户名',
    permission VARCHAR(20) DEFAULT 'read' COMMENT '权限级别：read/write/admin',
    status VARCHAR(20) DEFAULT 'active' COMMENT '状态：active/inactive/expired',
    share_link VARCHAR(255) COMMENT '分享链接',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    expires_at DATETIME COMMENT '过期时间',
    INDEX idx_resource_id (resource_id),
    INDEX idx_shared_by (shared_by),
    INDEX idx_shared_with (shared_with),
    INDEX idx_share_link (share_link)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资源分享表';

-- 插入测试数据
INSERT INTO resource_share (resource_id, shared_by, shared_with, permission, status, share_link) 
VALUES (13, 'jj11', 'student1', 'read', 'active', 'test123456'); 