# 本地服务调用配置说明

## ✅ 已完成的配置

所有远程 Feign 调用已配置为优先使用本地服务！

---

## 📋 配置清单

### 1. bbs-rest (端口 7012)

**配置文件**: `bbs-rest/src/main/resources/application.yml`
```yaml
local:
  services:
    manage-auth:
      name: ns-manage-auth
      url: http://127.0.0.1:7014  # 本地认证服务
```

**Feign Client**: `bbs-rest/src/main/java/.../UserServiceClient.java`
```java
@FeignClient(
    contextId = "restUserServiceClient",
    name = "${local.services.manage-auth.name:ns-manage-auth}",
    url = "${local.services.manage-auth.url:}",  // 支持直接URL调用
    path = "/user"
)
```

---

### 2. bbs-user (端口 7013)

**配置文件**: `bbs-user/bbs-user-service/src/main/resources/application.yml`
```yaml
local:
  services:
    manage-auth:
      name: ns-manage-auth
      url: http://127.0.0.1:7014  # 本地认证服务
```

**Feign Client**: `bbs-user/bbs-user-service/src/main/java/.../UserServiceClient.java`
```java
@FeignClient(
    contextId = "userUserServiceClient",
    name = "${local.services.manage-auth.name:ns-manage-auth}",
    url = "${local.services.manage-auth.url:}",  // 支持直接URL调用
    path = "/user"
)
```

---

### 3. bbs-article (端口 7011)

**配置文件**: `bbs-article/bbs-article-service/src/main/resources/application.yml`
```yaml
local:
  services:
    manage-auth:
      name: ns-manage-auth
      url: http://127.0.0.1:7014  # 本地认证服务
```

**Feign Client**: `bbs-article/bbs-article-service/src/main/java/.../UserServiceClient.java`
```java
@FeignClient(
    contextId = "articleUserServiceClient",
    name = "${local.services.manage-auth.name:ns-manage-auth}",
    url = "${local.services.manage-auth.url:}",  // 支持直接URL调用
    path = "/user"
)
```

---

## 🔧 工作原理

### 调用优先级

```
1. 如果配置了 url → 直接调用 http://127.0.0.1:7014
   ↓ (未配置 url)
2. 通过 Nacos 服务发现 → 查找 ns-manage-auth 服务
```

### 灵活切换

**使用本地服务**（当前配置）:
```yaml
local:
  services:
    manage-auth:
      url: http://127.0.0.1:7014  # 有此配置，直接调用本地
```

**使用远程服务**（注释掉 url）:
```yaml
local:
  services:
    manage-auth:
      # url: http://127.0.0.1:7014  # 注释掉，通过 Nacos 调用远程
```

---

## 🚀 启动顺序

### 推荐启动顺序

```bash
# 1. 中间件
MySQL、Redis、Nacos、MongoDB

# 2. 本地认证服务（必须先启动）
cd bbs-local-auth
mvn spring-boot:run

# 3. 其他服务（任意顺序）
bbs-article、bbs-user、bbs-rest
```

### 一键启动

使用 `start-all-services.bat` 脚本会自动按顺序启动。

---

## ✅ 验证配置

### 1. 检查服务启动

```bash
# 查看 bbs-local-auth 日志
# 应该看到: Tomcat started on port(s): 7014

# 查看其他服务日志
# 应该看到成功调用本地服务的日志
```

### 2. 测试接口调用

```bash
# 测试 bbs-rest 调用本地认证服务
curl http://localhost:7012/api/bbs/sso/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"123456"}'

# 测试 bbs-article 调用本地认证服务
curl http://localhost:7011/api/bbs/article/getList \
  -H "Content-Type: application/json" \
  -d '{}'
```

### 3. 查看调用日志

在 `bbs-local-auth` 的日志中应该看到：

```
INFO  - 批量获取用户信息: [1640, 1812, 2631]
INFO  - 获取所有用户列表
INFO  - 用户登录: admin
INFO  - Token验证
```

---

## 📊 调用关系图

```
┌─────────────────────────────────────────┐
│         bbs-local-auth (7014)           │
│      本地认证服务（必须首先启动）         │
│                                         │
│  - 用户登录/注册                         │
│  - Token验证                            │
│  - 用户信息查询                          │
│  - 访问记录                             │
│  - 通知管理                             │
└────────────┬────────────────────────────┘
             ↑ 调用
             │
    ┌────────┼────────┐
    │        │        │
┌───▼───┐ ┌─▼────┐ ┌─▼────┐
│rest   │ │user  │ │article│
│7012   │ │7013  │ │7011   │
└───────┘ └──────┘ └───────┘
```

---

## 🔍 故障排查

### 问题1: 连接拒绝

**错误**: `Connection refused: connect`

**原因**: bbs-local-auth 未启动

**解决**:
```bash
cd d:\L-work\bbs\bbs-springboot\bbs-local-auth
mvn spring-boot:run
```

---

### 问题2: 404 Not Found

**错误**: `FeignException$NotFound`

**原因**: 接口未实现

**解决**: 检查 `LocalAuthController` 是否实现了对应接口

---

### 问题3: 500 Internal Server Error

**错误**: `FeignException$InternalServerError`

**原因**: 返回类型不匹配或时间格式错误

**解决**: 确保返回 `UserDTO` 和 `UserListDTO` 类型

---

## 📝 配置说明

### url 配置的作用

| 配置 | 效果 | 使用场景 |
|------|------|---------|
| `url: http://127.0.0.1:7014` | 直接调用本地服务 | 本地开发 |
| 不配置 url | 通过 Nacos 服务发现 | 生产环境 |
| `url: ${DEV_AUTH_URL:}` | 环境变量控制 | 灵活切换 |

### 环境变量方式（可选）

```yaml
local:
  services:
    manage-auth:
      url: ${DEV_AUTH_URL:}  # 从环境变量读取
```

启动时设置环境变量：
```bash
set DEV_AUTH_URL=http://127.0.0.1:7014
mvn spring-boot:run
```

---

## 🎯 最佳实践

### 开发环境

```yaml
# application.yml (开发配置)
local:
  services:
    manage-auth:
      url: http://127.0.0.1:7014  # 固定本地地址
```

### 生产环境

```yaml
# application-prod.yml (生产配置)
local:
  services:
    manage-auth:
      # 不配置 url，使用 Nacos 服务发现
      name: ns-manage-auth
```

---

## 📦 修改文件清单

| 文件 | 修改内容 |
|------|---------|
| `bbs-rest/application.yml` | 添加 url 配置 |
| `bbs-rest/UserServiceClient.java` | 添加 url 属性 |
| `bbs-user/application.yml` | 添加 url 配置 |
| `bbs-user/UserServiceClient.java` | 添加 url 属性 |
| `bbs-article/application.yml` | 添加 url 配置 |
| `bbs-article/UserServiceClient.java` | 添加 url 属性 |

---

## ✨ 优势

✅ **完全本地化** - 所有认证调用都在本地  
✅ **快速响应** - 无需网络延迟  
✅ **独立开发** - 不依赖外部服务  
✅ **灵活切换** - 可随时切回远程服务  
✅ **向后兼容** - 不影响现有配置  

---

## 🎉 完成！

现在所有服务都会优先调用本地的 `bbs-local-auth` 服务！

**下一步**:
1. 启动 `bbs-local-auth` 服务
2. 重启其他服务（bbs-rest、bbs-user、bbs-article）
3. 观察日志，确认调用本地服务成功
4. 测试功能是否正常

---

*配置完成时间: 2026-04-23*
