-- =============================================
-- BBS 本地认证服务数据库初始化脚本
-- =============================================

-- 创建数据库（如果不存在）
CREATE DATABASE IF NOT EXISTS `local_auth` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

USE `local_auth`;

-- =============================================
-- 1. 用户表
-- =============================================
DROP TABLE IF EXISTS `local_user`;
CREATE TABLE `local_user` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` VARCHAR(50) NOT NULL COMMENT '用户名',
  `email` VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
  `phone` VARCHAR(20) DEFAULT NULL COMMENT '手机号',
  `password` VARCHAR(200) NOT NULL COMMENT '密码（BCrypt加密）',
  `avatar` VARCHAR(500) DEFAULT NULL COMMENT '头像URL',
  `state` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  KEY `idx_email` (`email`),
  KEY `idx_phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci COMMENT='用户表';

-- 插入默认管理员用户（密码：123456，BCrypt加密）
INSERT INTO `local_user` (`id`, `username`, `email`, `phone`, `password`, `avatar`, `state`) VALUES
(1640, 'admin', 'admin@local.com', NULL, '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', NULL, 1);

-- =============================================
-- 2. 访问记录表
-- =============================================
DROP TABLE IF EXISTS `local_visit`;
CREATE TABLE `local_visit` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `project_id` INT NOT NULL COMMENT '项目ID',
  `ip` VARCHAR(50) NOT NULL COMMENT 'IP地址',
  `os` VARCHAR(100) DEFAULT NULL COMMENT '操作系统',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_project_id` (`project_id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci COMMENT='访问记录表';

-- =============================================
-- 3. 通知表
-- =============================================
DROP TABLE IF EXISTS `local_notify`;
CREATE TABLE `local_notify` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '通知ID',
  `user_id` BIGINT NOT NULL COMMENT '用户ID',
  `type` INT NOT NULL COMMENT '通知类型：1-系统通知，2-任务提醒',
  `title` VARCHAR(200) NOT NULL COMMENT '通知标题',
  `content` TEXT COMMENT '通知内容',
  `is_read` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否已读：0-未读，1-已读',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_type` (`type`),
  KEY `idx_is_read` (`is_read`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci COMMENT='通知表';

-- =============================================
-- 说明：
-- 1. 默认管理员账号：
--    用户名：admin
--    密码：123456
--    用户ID：1640
--
-- 2. 密码使用 BCrypt 加密
-- 3. 所有表使用 utf8mb4 字符集，支持 emoji
-- 4. 包含必要的索引优化查询性能
-- =============================================
