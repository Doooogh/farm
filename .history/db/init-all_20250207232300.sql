-- 创建数据库
-- 认证服务数据库
CREATE DATABASE IF NOT EXISTS farm_cloud_auth
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_general_ci;

-- 用户服务数据库
CREATE DATABASE IF NOT EXISTS farm_cloud_user
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_general_ci;

-- 系统服务数据库
CREATE DATABASE IF NOT EXISTS farm_cloud_system
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_general_ci;

-- 创建数据库用户并授权
-- 认证服务用户
CREATE USER IF NOT EXISTS '${db.auth.username}'@'%' IDENTIFIED BY '${db.auth.password}';
GRANT ALL PRIVILEGES ON farm_cloud_auth.* TO '${db.auth.username}'@'%';

-- 用户服务用户
CREATE USER IF NOT EXISTS '${db.user.username}'@'%' IDENTIFIED BY '${db.user.password}';
GRANT ALL PRIVILEGES ON farm_cloud_user.* TO '${db.user.username}'@'%';

-- 系统服务用户
CREATE USER IF NOT EXISTS '${db.system.username}'@'%' IDENTIFIED BY '${db.system.password}';
GRANT ALL PRIVILEGES ON farm_cloud_system.* TO '${db.system.username}'@'%';

FLUSH PRIVILEGES;

-- 使用认证服务数据库
USE farm_cloud_auth;

-- 用户表
CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    username VARCHAR(50) NOT NULL COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    nickname VARCHAR(50) COMMENT '昵称',
    email VARCHAR(100) COMMENT '邮箱',
    phone VARCHAR(20) COMMENT '手机号',
    wechat_openid VARCHAR(50) COMMENT '微信OpenID',
    mfa_secret_key VARCHAR(50) COMMENT 'MFA密钥',
    status TINYINT DEFAULT 1 COMMENT '状态(0:禁用,1:正常)',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT(1) DEFAULT 0 COMMENT '是否删除',
    UNIQUE KEY uk_username (username),
    UNIQUE KEY uk_phone (phone),
    UNIQUE KEY uk_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 角色表
CREATE TABLE IF NOT EXISTS sys_role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    code VARCHAR(50) NOT NULL COMMENT '角色编码',
    name VARCHAR(50) NOT NULL COMMENT '角色名称',
    description VARCHAR(200) COMMENT '角色描述',
    status TINYINT DEFAULT 1 COMMENT '状态(0:禁用,1:正常)',
    sort INT DEFAULT 0 COMMENT '排序',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT(1) DEFAULT 0 COMMENT '是否删除',
    UNIQUE KEY uk_code (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- 权限表
CREATE TABLE IF NOT EXISTS sys_permission (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    code VARCHAR(100) NOT NULL COMMENT '权限编码',
    name VARCHAR(100) NOT NULL COMMENT '权限名称',
    type VARCHAR(20) NOT NULL COMMENT '权限类型（menu:菜单,button:按钮）',
    parent_id BIGINT COMMENT '父权限ID',
    path VARCHAR(200) COMMENT '权限路径',
    component VARCHAR(200) COMMENT '组件路径',
    icon VARCHAR(100) COMMENT '权限图标',
    sort INT DEFAULT 0 COMMENT '排序',
    status TINYINT DEFAULT 1 COMMENT '状态(0:禁用,1:正常)',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT(1) DEFAULT 0 COMMENT '是否删除',
    UNIQUE KEY uk_code (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权限表';

-- 用户角色关联表
CREATE TABLE IF NOT EXISTS sys_user_role (
    user_id BIGINT NOT NULL COMMENT '用户ID',
    role_code VARCHAR(50) NOT NULL COMMENT '角色编码',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (user_id, role_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

-- 角色权限关联表
CREATE TABLE IF NOT EXISTS sys_role_permission (
    role_code VARCHAR(50) NOT NULL COMMENT '角色编码',
    permission_code VARCHAR(100) NOT NULL COMMENT '权限编码',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (role_code, permission_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色权限关联表';

-- 登录日志表
CREATE TABLE IF NOT EXISTS sys_login_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    username VARCHAR(50) COMMENT '用户名',
    ip VARCHAR(50) COMMENT 'IP地址',
    location VARCHAR(100) COMMENT '登录地点',
    browser VARCHAR(50) COMMENT '浏览器类型',
    os VARCHAR(50) COMMENT '操作系统',
    status TINYINT COMMENT '登录状态(0:失败,1:成功)',
    msg VARCHAR(200) COMMENT '提示消息',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='登录日志表';

-- 使用用户服务数据库
USE farm_cloud_user;

-- 用户扩展信息表
CREATE TABLE IF NOT EXISTS user_info (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    real_name VARCHAR(50) COMMENT '真实姓名',
    avatar VARCHAR(200) COMMENT '头像URL',
    gender TINYINT COMMENT '性别(0:未知,1:男,2:女)',
    birthday DATE COMMENT '生日',
    address VARCHAR(200) COMMENT '地址',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted TINYINT(1) DEFAULT 0 COMMENT '是否删除',
    UNIQUE KEY uk_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户扩展信息表';

-- 使用系统服务数据库
USE farm_cloud_system;

-- 操作日志表
CREATE TABLE IF NOT EXISTS sys_operation_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    user_id BIGINT COMMENT '操作用户ID',
    username VARCHAR(50) COMMENT '操作用户名',
    operation VARCHAR(50) COMMENT '操作类型',
    method VARCHAR(200) COMMENT '请求方法',
    params TEXT COMMENT '请求参数',
    time BIGINT COMMENT '执行时长(毫秒)',
    ip VARCHAR(50) COMMENT 'IP地址',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';

-- 初始数据
USE farm_cloud_auth;

-- 插入管理员用户(密码: admin123)
INSERT INTO sys_user (username, password, nickname, status) VALUES 
('admin', '$2a$10$ySG2lkvjFHY5O0./CPIE1OI8VJsuKYEzOYzqIa7AJR6sEgSzUFOAm', '系统管理员', 1);

-- 插入基础角色
INSERT INTO sys_role (code, name, description, status, sort) VALUES 
('ROLE_ADMIN', '管理员', '系统管理员', 1, 0),
('ROLE_USER', '普通用户', '普通用户', 1, 1);

-- 插入基础权限
INSERT INTO sys_permission (code, name, type, parent_id, path, sort, status) VALUES 
('system', '系统管理', 'menu', 0, '/system', 1, 1),
('system:user', '用户管理', 'menu', 1, '/system/user', 1, 1),
('system:user:list', '用户列表', 'button', 2, '', 1, 1),
('system:user:add', '添加用户', 'button', 2, '', 2, 1),
('system:user:edit', '编辑用户', 'button', 2, '', 3, 1),
('system:user:delete', '删除用户', 'button', 2, '', 4, 1),
('system:role', '角色管理', 'menu', 1, '/system/role', 2, 1),
('system:permission', '权限管理', 'menu', 1, '/system/permission', 3, 1);

-- 初始用户角色关联
INSERT INTO sys_user_role (user_id, role_code) VALUES 
(1, 'ROLE_ADMIN'),
(1, 'ROLE_USER');

-- 初始角色权限关联
INSERT INTO sys_role_permission (role_code, permission_code) VALUES 
('ROLE_ADMIN', 'system'),
('ROLE_ADMIN', 'system:user'),
('ROLE_ADMIN', 'system:user:list'),
('ROLE_ADMIN', 'system:user:add'),
('ROLE_ADMIN', 'system:user:edit'),
('ROLE_ADMIN', 'system:user:delete'),
('ROLE_ADMIN', 'system:role'),
('ROLE_ADMIN', 'system:permission'),
('ROLE_USER', 'system:user:list'); 