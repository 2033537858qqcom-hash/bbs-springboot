# BBS 本地认证服务 (bbs-local-auth)

## 📖 概述

`bbs-local-auth` 是一个轻量级的本地认证服务，用于替代外部的 `ns-manage-auth`（南生运营系统）。

这个服务提供了 BBS 项目所需的所有认证相关接口，让您可以在完全独立的环境下开发和测试，不依赖任何外部服务。

## 🎯 功能特性

### 1. 用户认证
- ✅ 用户注册
- ✅ 用户登录
- ✅ Token 验证
- ✅ 用户登出
- ✅ JWT Token 生成

### 2. 用户管理
- ✅ 获取用户信息（ByID/Email/Phone）
- ✅ 更新用户信息
- ✅ 上传头像
- ✅ 绑定/解绑邮箱
- ✅ 绑定/解绑手机号
- ✅ 修改密码
- ✅ 重置密码

### 3. 访问记录
- ✅ 创建访问记录
- ✅ 统计访问量

### 4. 通知系统
- ✅ 获取通知列表
- ✅ 标记已读
- ✅ 未读数量统计

### 5. 权限控制
- ✅ URL 访问权限检查（本地开发默认放行）

## 🚀 快速启动

### 方式1：使用一键启动脚本（推荐）

```bash
# 在项目根目录执行
start-all-services.bat
```

这会自动启动包括 `bbs-local-auth` 在内的所有服务。

### 方式2：手动启动

```bash
cd bbs-local-auth
mvn spring-boot:run
```

### 方式3：IDE 启动

在 IDEA 中直接运行 `LocalAuthApplication.java`

## 📋 服务信息

- **端口**: 7014
- **服务名**: ns-manage-auth
- **上下文**: 无（根路径）

## 🔗 API 文档

启动后访问：http://localhost:7014

### 主要接口

#### 用户认证

```
POST /user/register          # 用户注册
POST /user/login             # 用户登录
GET  /user/logout            # 用户登出
GET  /user/verify-token      # 验证Token
GET  /user/is-expired        # 检查Token是否过期
GET  /user/token-user        # 根据Token获取用户信息
```

#### 用户信息

```
GET  /user/by-id             # 根据ID获取用户
GET  /user/by-email          # 根据邮箱获取用户
GET  /user/by-phone          # 根据手机号获取用户
POST /user/update-user-basic-info  # 更新用户信息
POST /user/upload-user-picture     # 上传头像
```

#### 访问记录

```
POST /visit/create           # 创建访问记录
GET  /visit/total            # 获取总访问量
```

#### 通知

```
GET  /notify/list            # 获取通知列表
POST /notify/have-read       # 标记已读
POST /notify/mark-read       # 批量标记已读
GET  /notify/not-read-count  # 获取未读数量
```

#### 权限

```
GET  /url-access-right/check # 检查URL访问权限
```

## ⚙️ 配置说明

配置文件：`src/main/resources/application.yml`

### 数据库配置

```yaml
spring:
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/open_bbs
    username: root
    password: 123456  # 修改为你的密码
```

### Nacos 配置

```yaml
spring:
  cloud:
    nacos:
      discovery:
        server-addr: 127.0.0.1:8848
```

### JWT 配置

```yaml
jwt:
  secret: bbs-local-auth-secret-key-2024  # 可自定义
  expiration: 86400000  # Token有效期：24小时
```

## 📊 数据存储

### 简化实现

为了简化本地开发，当前版本使用**内存存储**而非数据库：

- 用户数据存储在 `Map<Long, UserDTO>`
- Token 数据存储在 `Map<String, UserSsoDTO>`
- 访问计数使用 `AtomicLong`

这意味着：
- ✅ 无需数据库表结构
- ✅ 启动即可使用
- ⚠️ 重启服务后数据会丢失
- ⚠️ 适合开发测试，不适合生产环境

### 默认用户

服务启动时会自动创建一个默认管理员用户：

```
用户ID: 1640
用户名: admin
邮箱: admin@local.com
密码: 123456
```

## 🔧 与其他服务的关系

### 服务依赖图

```
bbs-rest (7012)
  └── Feign → ns-manage-auth (7014) [bbs-local-auth]
       ├── /user/*         # 用户认证
       ├── /visit/*        # 访问记录
       ├── /notify/*       # 通知系统
       └── /url-access-right/*  # 权限检查

bbs-local-auth (7014)
  ├── Nacos (服务注册)
  └── 内存存储 (用户/Token数据)
```

### 服务注册

启动后，在 Nacos 控制台可以看到：

```
服务名: ns-manage-auth
实例: 127.0.0.1:7014
```

## 💡 使用场景

### ✅ 适合

1. **本地开发** - 无需依赖外部服务
2. **功能测试** - 快速验证功能
3. **接口调试** - 独立测试接口
4. **学习研究** - 理解认证流程

### ❌ 不适合

1. **生产环境** - 使用内存存储，数据不安全
2. **多实例部署** - 数据不共享
3. **持久化需求** - 重启后数据丢失

## 🔐 安全说明

### 当前实现

- JWT Secret 使用固定值（可在配置中修改）
- Token 有效期 24 小时
- 密码未加密存储（仅用于本地测试）

### 生产环境建议

如果要在生产环境使用，需要：
1. 使用数据库持久化
2. 密码加密存储（BCrypt）
3. 使用更强的 JWT Secret
4. 实现完善的权限控制
5. 添加验证码等安全机制

## 🐛 常见问题

### Q1: 服务启动失败？

**检查**：
1. 端口 7014 是否被占用
2. Nacos 是否启动
3. MySQL 是否启动（虽然当前未使用）

### Q2: 登录后无法获取用户信息？

**检查**：
1. Token 是否正确传递
2. Token 是否过期
3. 查看服务日志

### Q3: Feign 调用仍然返回 503？

**解决**：
1. 确认 bbs-local-auth 已启动
2. 检查 Nacos 中是否有 ns-manage-auth 服务
3. 确认 bbs-rest 的配置指向正确的服务名

## 📝 扩展开发

如果需要添加新功能，可以：

1. **添加新的 Controller**
   ```java
   @RestController
   @RequestMapping("/new-feature")
   public class NewFeatureController {
       // 实现你的功能
   }
   ```

2. **使用数据库**
   - 添加 MyBatis Mapper
   - 创建数据库表
   - 替换内存存储

3. **完善权限系统**
   - 添加角色管理
   - 实现 RBAC 权限控制

## 🎓 学习资源

- JWT 官方文档：https://jwt.io/
- Spring Cloud OpenFeign：https://spring.io/projects/spring-cloud-openfeign
- Nacos 文档：https://nacos.io/zh-cn/docs/what-is-nacos.html

---

**祝开发顺利！** 🎉
