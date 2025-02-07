-- 创建认证服务数据库
CREATE DATABASE IF NOT EXISTS farm_cloud_auth
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_general_ci;

-- 创建用户服务数据库
CREATE DATABASE IF NOT EXISTS farm_cloud_user
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_general_ci;

-- 创建系统服务数据库
CREATE DATABASE IF NOT EXISTS farm_cloud_system
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_general_ci;

-- 创建用户并授权
CREATE USER IF NOT EXISTS '${db.username}'@'%' IDENTIFIED BY '${db.password}';
GRANT ALL PRIVILEGES ON farm_cloud_*.* TO '${db.username}'@'%';
FLUSH PRIVILEGES; 