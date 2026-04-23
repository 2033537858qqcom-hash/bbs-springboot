# 🚀 BBS 本地认证服务 - 快速启动

## ⚡ 5分钟快速上手

### 第一步：初始化数据库（1分钟）

```bash
# 方法1: 使用命令行
mysql -u root -pliang < d:/L-work/bbs/bbs-springboot/bbs-local-auth/db/init.sql

# 方法2: 手动执行
mysql -u root -p
source d:/L-work/bbs/bbs-springboot/bbs-local-auth/db/init.sql
```

### 第二步：启动服务（2分钟）

```bash
# 进入项目目录
cd d:\L-work\bbs\bbs-springboot\bbs-local-auth

# 启动服务
mvn spring-boot:run
```

或使用一键启动：
```bash
cd d:\L-work\bbs\bbs-springboot
start-all-services.bat
```

### 第三步：测试验证（2分钟）

```bash
# 1. 测试登录
curl -X POST http://localhost:7014/user/login ^
  -H "Content-Type: application/json" ^
  -d "{\"username\":\"admin\",\"password\":\"123456\"}"

# 返回示例:
# {"userId":1640,"token":"eyJhbG..."}

# 2. 保存Token，测试验证（替换YOUR_TOKEN）
curl http://localhost:7014/user/verify-token?token=YOUR_TOKEN

# 返回: true

# 3. 测试批量获取用户
curl "http://localhost:7014/user/by-ids?userIds=1640"

# 返回: 用户信息列表
```

---

## 📋 前置条件检查

### ✅ 必须运行的服务

```bash
# 1. MySQL (端口 3306)
mysql -u root -p -e "SELECT 1"

# 2. Redis (端口 6379)
redis-cli ping
# 应返回: PONG

# 3. Nacos (端口 8848)
# 浏览器访问: http://localhost:8848/nacos
# 用户名/密码: nacos/nacos
```

### ❌ 如果某个服务未运行

**MySQL未运行**:
```bash
# Windows服务启动
net start MySQL80

# 或使用Docker
docker run -d --name mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=liang mysql:8.0
```

**Redis未运行**:
```bash
# Windows
redis-server

# 或使用Docker
docker run -d --name redis -p 6379:6379 redis:latest
```

**Nacos未运行**:
```bash
# 下载Nacos: https://github.com/alibaba/nacos/releases
# 解压后执行
cd nacos/bin
startup.cmd -m standalone
```

---

## 🎯 核心功能

### 1. 用户认证

| 功能 | 接口 | 方法 | 说明 |
|------|------|------|------|
| 登录 | `/user/login` | POST | 用户名+密码登录 |
| 注册 | `/user/register` | POST | 新用户注册 |
| 登出 | `/user/logout` | GET | 退出登录 |
| 验证Token | `/user/verify-token` | GET | 检查Token有效性 |

### 2. 用户查询

| 功能 | 接口 | 方法 | 说明 |
|------|------|------|------|
| 根据ID查询 | `/user/get` | GET | 单个用户信息 |
| 批量查询 | `/user/by-ids` | GET | 批量获取用户 |
| Token获取用户 | `/user/token-user` | GET | 根据Token获取用户 |

### 3. 访问记录

| 功能 | 接口 | 方法 | 说明 |
|------|------|------|------|
| 创建记录 | `/visit/create` | POST | 记录访问 |
| 统计总量 | `/visit/total` | GET | 总访问量 |

### 4. 通知管理

| 功能 | 接口 | 方法 | 说明 |
|------|------|------|------|
| 未读数量 | `/notify/not-read-count` | GET | 未读通知数 |
| 通知列表 | `/notify/list` | GET | 通知列表 |
| 标记已读 | `/notify/have-read` | POST | 标记单个已读 |

### 5. 权限检查

| 功能 | 接口 | 方法 | 说明 |
|------|------|------|------|
| URL权限 | `/url-access-right/check` | GET | 检查访问权限 |

---

## 🔧 配置修改

### 数据库配置

文件: `src/main/resources/application.yml`

```yaml
spring:
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/local_auth
    username: root
    password: liang  # 修改为你的密码
```

### Redis配置

```yaml
spring:
  data:
    redis:
      host: 127.0.0.1
      port: 6379
      password:  # 如果有密码，填写这里
```

### JWT配置

```yaml
jwt:
  secret: your-secret-key  # 修改密钥
  expiration: 86400000     # Token有效期(毫秒)
```

---

## 📊 数据说明

### 默认账号

| 用户名 | 密码 | 用户ID | 邮箱 |
|--------|------|--------|------|
| admin | 123456 | 1640 | admin@local.com |

### 数据库表

```
local_auth (数据库)
├─ local_user (用户表)
├─ local_visit (访问记录表)
└─ local_notify (通知表)
```

---

## 🐛 常见问题

### Q1: 编译失败

```
错误: 找不到符号
解决: mvn clean compile -U
```

### Q2: 数据库连接失败

```
错误: Communications link failure
解决:
1. 检查MySQL是否启动
2. 检查数据库名: local_auth
3. 检查用户名密码
```

### Q3: Redis连接失败

```
错误: Cannot get Jedis connection
解决:
1. redis-cli ping 测试
2. 检查host和port配置
```

### Q4: Token验证失败

```
错误: verifyToken 返回 false
解决:
1. 确认Token正确
2. 检查Redis中是否有该Token
3. 重新登录获取新Token
```

### Q5: 端口被占用

```
错误: Port 7014 was already in use
解决:
1. netstat -ano | findstr ":7014"
2. taskkill /PID <PID> /F
3. 或修改配置中的端口
```

---

## 📖 详细文档

- [完整实现指南](COMPLETE_GUIDE.md) - 架构、数据库、API详解
- [详细对接文档](INTEGRATION_GUIDE.md) - 接口说明、调用示例
- [快速参考](QUICK_REFERENCE.md) - 常用命令速查
- [使用说明](README.md) - 基本介绍

---

## ✅ 验证清单

启动后检查以下项目：

- [ ] MySQL 运行正常
- [ ] Redis 运行正常
- [ ] Nacos 运行正常
- [ ] 数据库 local_auth 已创建
- [ ] 服务启动成功（端口 7014）
- [ ] Nacos 中显示 ns-manage-auth 服务
- [ ] 登录接口返回 Token
- [ ] Token 验证返回 true
- [ ] 批量获取用户返回数据

---

## 🎉 完成！

现在您的本地认证服务已经完整运行，完全替代了原来的 ns-manage-auth 服务！

**支持的所有功能**:
✅ MySQL 持久化存储  
✅ Redis 缓存优化  
✅ JWT Token 认证  
✅ BCrypt 密码加密  
✅ 用户管理  
✅ 访问记录  
✅ 通知管理  
✅ URL权限检查  

**开始使用吧！** 🚀
