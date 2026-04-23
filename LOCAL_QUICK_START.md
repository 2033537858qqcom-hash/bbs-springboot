# BBS 本地开发快速开始

## 🚀 快速开始

### 前置条件

确保以下中间件已在本地或 Docker 中运行（建议统一使用 `localhost` 访问前端和 API）：

- ✅ Nacos (localhost:8848)
- ✅ MySQL (localhost:3306)
- ✅ Redis (localhost:6379)
- ✅ MongoDB (localhost:27017)

### 一键启动

```bash
# 1. 检查中间件状态
check-middleware.bat

# 2. 启动所有服务（会自动编译）
start-all-services.bat

# 3. 停止所有服务
stop-all-services.bat
```

## 📋 服务说明

| 服务 | 端口 | 说明 |
|------|------|------|
| bbs-local-auth | 7014 | 本地认证服务（新增）✅ |
| bbs-rest | 7012 | API 网关服务 |
| bbs-article | 7011 | 文章服务 |
| bbs-user | 7013 | 用户服务 |

**重要说明**：
- ✅ **bbs-local-auth** 是新增的本地认证服务，替代外部的 ns-manage-auth
- ✅ 现在项目完全独立，不依赖任何外部服务
- ✅ 一键启动即可使用所有功能

## 🔗 访问地址

启动成功后，访问以下地址：

- **Nacos 控制台**: http://127.0.0.1:8848/nacos (nacos/nacos)
- **bbs-local-auth**: http://localhost:7014 （本地认证服务）
- **bbs-rest API 文档**: http://localhost:7012/api/doc.html
- **bbs-article API 文档**: http://localhost:7011/doc.html
- **bbs-user API 文档**: http://localhost:7013/doc.html
- **前端开发地址**: http://localhost:8082

## ⚙️ 配置说明

### 数据库配置

如果 MySQL 用户名密码不是 `root/123456`，请修改：

- `bbs-article/bbs-article-service/src/main/resources/application.yml`
- `bbs-user/bbs-user-service/src/main/resources/application.yml`

```yaml
spring:
  datasource:
    username: your_username  # 修改为你的用户名
    password: your_password  # 修改为你的密码
```

### Redis/MongoDB 配置

如果需要密码，请在相应的 `application.yml` 中配置。

## 📖 详细文档

完整的本地开发指南请查看：[LOCAL_DEVELOPMENT_GUIDE.md](LOCAL_DEVELOPMENT_GUIDE.md)

## 🛠️ 常用命令

### 编译项目

```bash
mvn clean install -DskipTests
```

### 单独启动某个服务

```bash
# 启动用户服务
cd bbs-user/bbs-user-service
mvn spring-boot:run

# 启动文章服务
cd bbs-article/bbs-article-service
mvn spring-boot:run

# 启动 API 服务
cd bbs-rest
mvn spring-boot:run
```

### 打包部署

```bash
mvn clean package -DskipTests

# 运行 JAR
java -jar bbs-rest/target/bbs-rest.jar
```

## ❓ 常见问题

### Q: 服务启动失败？

1. 运行 `check-middleware.bat` 检查中间件状态
2. 确保 Nacos、MySQL、Redis、MongoDB 都已启动
3. 查看日志文件：`logs/bbs-*/`

### Q: Feign 调用 503 错误？

- 确保所有服务都已启动并注册到 Nacos
- 访问 Nacos 控制台查看服务列表

### Q: 数据库连接失败？

- 检查 MySQL 是否启动
- 确认 `application.yml` 中的用户名密码正确

## 📞 获取帮助

详细问题排查请查看 [LOCAL_DEVELOPMENT_GUIDE.md](LOCAL_DEVELOPMENT_GUIDE.md) 的常见问题部分。

---

**祝开发顺利！** 🎉
