# BBS 本地认证服务 - 完整实现指南

## 📋 目录

1. [架构说明](#架构说明)
2. [数据库配置](#数据库配置)
3. [Redis配置](#redis配置)
4. [启动步骤](#启动步骤)
5. [功能清单](#功能清单)
6. [API接口文档](#api接口文档)
7. [测试验证](#测试验证)

---

## 架构说明

### 技术栈

| 组件 | 技术 | 版本 |
|------|------|------|
| 框架 | Spring Boot | 3.5.11 |
| ORM | MyBatis | 3.0.5 |
| 数据库 | MySQL | 8.0+ |
| 缓存 | Redis | 6.0+ |
| 认证 | JWT | 3.15.0 |
| 加密 | BCrypt | Spring Security |

### 架构图

```
┌──────────────────────────────────────────────────┐
│                Controller 层                      │
│  - LocalAuthController (认证/用户管理)            │
│  - VisitController (访问记录)                     │
│  - NotifyController (通知管理)                    │
│  - UrlAccessRightController (权限检查)            │
└──────────────────┬───────────────────────────────┘
                   │
┌──────────────────▼───────────────────────────────┐
│                Service 层                         │
│  - UserService (用户业务逻辑)                     │
│    ├─ JWT Token 生成和验证                        │
│    ├─ BCrypt 密码加密                            │
│    └─ Redis 缓存管理                             │
└──────────────────┬───────────────────────────────┘
                   │
┌──────────────────▼───────────────────────────────┐
│              Mapper 层 (MyBatis)                  │
│  - UserMapper (用户数据访问)                      │
│  - VisitMapper (访问记录数据访问)                 │
│  - NotifyMapper (通知数据访问)                    │
└──────────────────┬───────────────────────────────┘
                   │
┌──────────────────▼───────────────────────────────┐
│              数据存储层                            │
│  - MySQL (持久化存储)                             │
│  - Redis (缓存/Token存储)                         │
└──────────────────────────────────────────────────┘
```

---

## 数据库配置

### 1. 初始化数据库

```bash
# 登录MySQL
mysql -u root -p

# 执行初始化脚本
source d:/L-work/bbs/bbs-springboot/bbs-local-auth/db/init.sql
```

或使用命令行一键执行：

```bash
mysql -u root -pliang < d:/L-work/bbs/bbs-springboot/bbs-local-auth/db/init.sql
```

### 2. 验证数据库

```sql
-- 切换到local_auth数据库
USE local_auth;

-- 查看表
SHOW TABLES;

-- 查看默认用户
SELECT id, username, email, state FROM local_user;

-- 应该看到：
-- +------+----------+-----------------+-------+
-- | id   | username | email           | state |
-- +------+----------+-----------------+-------+
-- | 1640 | admin    | admin@local.com |     1 |
-- +------+----------+-----------------+-------+
```

### 3. 数据库表说明

#### local_user (用户表)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键，自增 |
| username | VARCHAR(50) | 用户名，唯一 |
| email | VARCHAR(100) | 邮箱 |
| phone | VARCHAR(20) | 手机号 |
| password | VARCHAR(200) | BCrypt加密密码 |
| avatar | VARCHAR(500) | 头像URL |
| state | TINYINT(1) | 状态：0-禁用，1-启用 |
| create_time | DATETIME | 创建时间 |
| update_time | DATETIME | 更新时间 |

#### local_visit (访问记录表)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键，自增 |
| project_id | INT | 项目ID |
| ip | VARCHAR(50) | IP地址 |
| os | VARCHAR(100) | 操作系统 |
| create_time | DATETIME | 创建时间 |

#### local_notify (通知表)

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键，自增 |
| user_id | BIGINT | 用户ID |
| type | INT | 通知类型 |
| title | VARCHAR(200) | 通知标题 |
| content | TEXT | 通知内容 |
| is_read | TINYINT(1) | 是否已读 |
| create_time | DATETIME | 创建时间 |

---

## Redis配置

### 1. 检查Redis是否运行

```bash
redis-cli ping
# 应该返回：PONG
```

### 2. Redis中存储的数据

```
Key格式:
- local:auth:user:{userId}        # 用户信息缓存
- local:auth:token:{token}        # Token存储

过期时间:
- 用户缓存: 24小时
- Token: 24小时（与JWT一致）
```

### 3. 查看Redis数据

```bash
# 查看所有Key
redis-cli KEYS "local:auth:*"

# 查看某个Token
redis-cli GET "local:auth:token:eyJhbG..."

# 查看Key的剩余过期时间
redis-cli TTL "local:auth:token:eyJhb..."
```

---

## 启动步骤

### 方式1: 使用一键启动脚本

```bash
cd d:\L-work\bbs\bbs-springboot
start-all-services.bat
```

### 方式2: 单独启动

```bash
cd d:\L-work\bbs\bbs-springboot\bbs-local-auth
mvn spring-boot:run
```

### 方式3: 使用JAR包

```bash
# 先编译打包
cd d:\L-work\bbs\bbs-springboot\bbs-local-auth
mvn clean package -DskipTests

# 运行JAR
java -jar target/bbs-local-auth-1.0.0.jar
```

### 启动验证

```bash
# 检查服务是否启动
curl http://localhost:7014/user/verify-token?token=test

# 查看 Nacos 控制台
# 浏览器访问: http://localhost:8848/nacos
# 用户名/密码: nacos/nacos
# 查看服务列表中的 ns-manage-auth
```

---

## 功能清单

### ✅ 已实现功能

| 模块 | 功能 | 说明 |
|------|------|------|
| **用户认证** | 用户登录 | 用户名+密码，返回JWT Token |
| | 用户注册 | 创建新用户，自动分配ID |
| | Token验证 | 验证JWT签名和Redis存储 |
| | 用户登出 | 删除Redis中的Token |
| **用户管理** | 根据ID查询 | 支持Redis缓存 |
| | 批量查询 | 支持IN查询，返回用户列表 |
| | 根据用户名查询 | 用于登录验证 |
| **访问记录** | 创建记录 | 记录IP、OS等信息 |
| | 统计总量 | 总访问量统计 |
| | 统计今日 | 今日访问量统计 |
| **通知管理** | 创建通知 | 支持不同类型 |
| | 通知列表 | 分页查询 |
| | 标记已读 | 单个/批量标记 |
| | 未读数量 | 统计未读通知 |
| **权限检查** | URL权限 | 本地开发默认放行 |

### 🔧 数据存储

| 数据 | 存储位置 | 说明 |
|------|---------|------|
| 用户信息 | MySQL + Redis缓存 | 持久化+高性能读取 |
| Token | Redis | 24小时过期 |
| 访问记录 | MySQL | 持久化存储 |
| 通知 | MySQL | 持久化存储 |

---

## API接口文档

### 1. 用户登录

**POST** `/user/login`

**请求体**:
```json
{
  "username": "admin",
  "password": "123456"
}
```

**响应**:
```json
{
  "userId": 1640,
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxNjQwIiwidXNlcklkIjoxNjQwLCJ1c2VybmFtZSI6ImFkbWluIiwiaWF0IjoxNjE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"
}
```

### 2. 用户注册

**POST** `/user/register`

**请求体**:
```json
{
  "username": "newuser",
  "email": "user@example.com",
  "password": "123456"
}
```

**响应**:
```json
{
  "userId": 1234567890,
  "token": "eyJhbG..."
}
```

### 3. 验证Token

**GET** `/user/verify-token`

**参数**:
```
token=eyJhbG...
```

**响应**:
```json
true  // 或 false
```

### 4. 批量获取用户

**GET** `/user/by-ids`

**参数**:
```
userIds=1640&userIds=1812&userIds=1234
```

**响应**:
```json
[
  {
    "id": 1640,
    "username": "admin",
    "email": "admin@local.com",
    "phone": null,
    "avatar": null,
    "state": true,
    "createTime": "2024-01-01T00:00:00",
    "updateTime": "2024-01-01T00:00:00"
  }
]
```

### 5. 创建访问记录

**POST** `/visit/create`

**请求体**:
```json
{
  "projectId": 1,
  "ip": "127.0.0.1",
  "os": "Windows 10"
}
```

**响应**:
```json
true
```

### 6. 获取总访问量

**GET** `/visit/total`

**响应**:
```json
1234
```

### 7. 获取未读通知数量

**GET** `/notify/not-read-count`

**参数**:
```
userId=1640&type=1
```

**响应**:
```json
5
```

### 8. 检查URL权限

**GET** `/url-access-right/check`

**参数**:
```
uri=/api/bbs/article/create&attribute={}
```

**响应**:
```json
true
```

---

## 测试验证

### 完整测试流程

```bash
# 1. 初始化数据库
mysql -u root -pliang < d:/L-work/bbs/bbs-springboot/bbs-local-auth/db/init.sql

# 2. 启动服务
cd d:/L-work/bbs/bbs-springboot/bbs-local-auth
mvn spring-boot:run

# 3. 测试登录
curl -X POST http://localhost:7014/user/login ^
  -H "Content-Type: application/json" ^
  -d "{\"username\":\"admin\",\"password\":\"123456\"}"

# 4. 保存返回的Token，测试验证
curl http://localhost:7014/user/verify-token?token=YOUR_TOKEN

# 5. 测试批量获取用户
curl "http://localhost:7014/user/by-ids?userIds=1640"

# 6. 测试访问记录
curl -X POST http://localhost:7014/visit/create ^
  -H "Content-Type: application/json" ^
  -d "{\"projectId\":1,\"ip\":\"127.0.0.1\",\"os\":\"Windows\"}"

# 7. 查看访问量
curl http://localhost:7014/visit/total

# 8. 检查Redis
redis-cli KEYS "local:auth:*"
```

### 数据库验证

```sql
-- 查看用户表
SELECT * FROM local_user;

-- 查看访问记录
SELECT COUNT(*) FROM local_visit;
SELECT * FROM local_visit ORDER BY create_time DESC LIMIT 10;

-- 查看通知
SELECT * FROM local_notify;
```

---

## 密码管理

### BCrypt 密码加密

系统使用 BCrypt 算法加密密码，具有以下特点：

1. **自动加盐** - 每次加密结果不同
2. **单向加密** - 无法解密，只能验证
3. **抗暴力破解** - 计算成本高

### 生成BCrypt密码

```java
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
String encodedPassword = encoder.encode("123456");
System.out.println(encodedPassword);
// 输出: $2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi
```

### 默认账号密码

| 用户名 | 密码 | 用户ID |
|--------|------|--------|
| admin | 123456 | 1640 |

---

## 性能优化

### Redis缓存策略

```
查询用户:
1. 先查Redis缓存 (5ms)
2. 缓存未命中查MySQL (50ms)
3. 查询结果写入Redis

Token验证:
1. 验证JWT签名 (1ms)
2. 检查Redis是否存在 (5ms)
```

### 数据库索引

```sql
-- 用户表索引
uk_username (username)  -- 唯一索引，登录查询
idx_email (email)       -- 普通索引，邮箱查询
idx_phone (phone)       -- 普通索引，手机查询

-- 访问记录表索引
idx_project_id (project_id)
idx_create_time (create_time)

-- 通知表索引
idx_user_id (user_id)
idx_type (type)
idx_is_read (is_read)
```

---

## 常见问题

### Q1: 数据库连接失败

```
错误: Communications link failure
解决: 
1. 检查MySQL是否启动
2. 检查数据库名是否正确 (local_auth)
3. 检查用户名密码 (root/liang)
```

### Q2: Redis连接失败

```
错误: Cannot get Jedis connection
解决:
1. 检查Redis是否启动
2. redis-cli ping 测试连接
3. 检查配置中的host和port
```

### Q3: Token验证失败

```
错误: verifyToken 返回 false
解决:
1. 检查Token是否正确传递
2. 检查Redis中是否存在该Token
3. 检查JWT Secret是否一致
```

### Q4: 用户登录失败

```
错误: 密码错误
解决:
1. 确认密码是 BCrypt 加密的
2. 查看数据库中密码字段
3. 使用默认密码: 123456
```

---

## 扩展开发

### 添加新的用户字段

1. 修改数据库表
```sql
ALTER TABLE local_user ADD COLUMN nickname VARCHAR(100) COMMENT '昵称';
```

2. 修改实体类
```java
@Data
public class User {
    // ... 其他字段
    private String nickname;
}
```

3. 修改Mapper
```java
@Update("UPDATE local_user SET nickname=#{nickname} WHERE id=#{id}")
int updateNickname(@Param("id") Long id, @Param("nickname") String nickname);
```

### 添加短信验证码登录

```java
@PostMapping("/login/sms")
public Map<String, Object> loginBySms(@RequestParam String phone, 
                                       @RequestParam String code) {
    // 1. 验证短信验证码
    // 2. 根据手机号查询用户
    // 3. 生成Token
    // 4. 返回结果
}
```

---

## 总结

✅ **完整的数据库支持** - MySQL持久化存储  
✅ **Redis缓存优化** - 高性能Token和用户查询  
✅ **BCrypt密码加密** - 安全的密码存储  
✅ **JWT认证机制** - 标准的Token生成和验证  
✅ **完善的错误处理** - 友好的错误提示  
✅ **详细的日志记录** - 便于问题排查  

现在您拥有一个**功能完整、性能优良、安全可靠**的本地认证服务！🎉
