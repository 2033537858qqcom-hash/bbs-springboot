# BBS 本地认证服务 - 快速参考

## 🔗 基本信息

| 项目 | 值 |
|------|-----|
| 服务名 | ns-manage-auth |
| 端口 | 7014 |
| 访问地址 | http://localhost:7014 |
| Nacos 控制台 | http://localhost:8848/nacos (nacos/nacos) |

## 🚀 快速启动

```bash
# 启动服务
cd bbs-local-auth
mvn spring-boot:run

# 或使用一键启动
start-all-services.bat
```

## 📡 核心接口速查

### 认证相关

```bash
# 登录
curl -X POST http://localhost:7014/user/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"123456"}'

# 注册
curl -X POST http://localhost:7014/user/register \
  -H "Content-Type: application/json" \
  -d '{"username":"newuser","password":"123456"}'

# 验证Token
curl http://localhost:7014/user/verify-token?token=YOUR_TOKEN

# 获取用户信息
curl http://localhost:7014/user/token-user?token=YOUR_TOKEN

# 登出
curl http://localhost:7014/user/logout?token=YOUR_TOKEN
```

### 用户查询

```bash
# 批量获取用户
curl "http://localhost:7014/user/by-ids?userIds=1812&userIds=1640"
```

### 访问记录

```bash
# 创建访问记录
curl -X POST http://localhost:7014/visit/create \
  -H "Content-Type: application/json" \
  -d '{"projectId":1,"ip":"127.0.0.1","os":"Windows"}'

# 获取总访问量
curl http://localhost:7014/visit/total
```

### 通知

```bash
# 获取未读数量
curl "http://localhost:7014/notify/not-read-count?userId=1640&type=1"

# 获取通知列表
curl http://localhost:7014/notify/list
```

### 权限

```bash
# 检查URL权限
curl "http://localhost:7014/url-access-right/check?uri=/api/test&attribute={}"
```

## 🔑 JWT 配置

```yaml
Secret: bbs-local-auth-secret-key-2024
有效期: 86400000ms (24小时)
算法: HS256
```

## 📊 调用关系

```
bbs-rest (7012)
  ├─ LoginInterceptor → /user/verify-token
  ├─ LoginInterceptor → /user/token-user
  ├─ VisitInterceptor → /visit/create
  ├─ NotifyInterceptor → /notify/not-read-count
  └─ UrlAccessCheckInterceptor → /url-access-right/check

bbs-article (7011)
  └─ ArticleServiceImpl → /user/by-ids

bbs-user (7013)
  └─ UserServiceClient → /user/by-ids
```

## 🎯 默认用户

```
用户ID: 1640
用户名: admin (通过login接口传入)
说明: 首次登录自动创建
```

## ⚠️ 注意事项

1. **内存存储** - 重启服务后Token数据丢失
2. **Token传递** - 通过 Cookie `NS_ACCOUNT_SSO_COOKIE` 传递
3. **本地开发** - 仅用于开发测试，不建议生产使用
4. **权限放行** - URL权限检查默认返回 true

## 🐛 故障排查

```bash
# 检查服务是否运行
curl http://localhost:7014/user/verify-token?token=test

# 查看服务日志
tail -f logs/bbs-local-auth-info.log

# 查看 Nacos 注册
http://localhost:8848/nacos → 服务列表 → ns-manage-auth

# 查看端口占用
netstat -ano | findstr ":7014"
```

## 📝 文件位置

```
bbs-local-auth/
├── src/main/java/com/liang/local/auth/
│   ├── LocalAuthApplication.java          # 主启动类
│   └── controller/
│       ├── LocalAuthController.java       # 用户认证接口
│       ├── VisitController.java           # 访问记录接口
│       ├── NotifyController.java          # 通知接口
│       └── UrlAccessRightController.java  # 权限接口
├── src/main/resources/
│   └── application.yml                    # 配置文件
├── INTEGRATION_GUIDE.md                   # 详细对接文档
└── README.md                              # 使用说明
```

## 🔧 常用配置

### 修改端口
```yaml
# application.yml
server:
  port: 7014  # 改为其他端口
```

### 修改 JWT Secret
```yaml
# application.yml
jwt:
  secret: your-new-secret-key
  expiration: 86400000
```

### 修改数据库
```yaml
# application.yml
spring:
  datasource:
    username: root
    password: your_password
```

---

**更多详细信息请查看**: [INTEGRATION_GUIDE.md](INTEGRATION_GUIDE.md)
