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

-- 创建认证服务用户并授权
CREATE USER IF NOT EXISTS '${db.auth.username}'@'%' IDENTIFIED BY '${db.auth.password}';
GRANT ALL PRIVILEGES ON farm_cloud_auth.* TO '${db.auth.username}'@'%';

-- 创建用户服务用户并授权
CREATE USER IF NOT EXISTS '${db.user.username}'@'%' IDENTIFIED BY '${db.user.password}';
GRANT ALL PRIVILEGES ON farm_cloud_user.* TO '${db.user.username}'@'%';

-- 创建系统服务用户并授权
CREATE USER IF NOT EXISTS '${db.system.username}'@'%' IDENTIFIED BY '${db.system.password}';
GRANT ALL PRIVILEGES ON farm_cloud_system.* TO '${db.system.username}'@'%';

FLUSH PRIVILEGES; 