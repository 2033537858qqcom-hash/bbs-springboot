# BBS 项目本地运行指南

## 📋 本地中间件清单

项目已在本地/Docker 中部署的中间件：

| 中间件 | 地址 | 端口 | 说明 |
|--------|------|------|------|
| MySQL | 127.0.0.1 | 3306 | 数据库 open_bbs |
| Redis | 127.0.0.1 | 6379 | 缓存服务 |
| MongoDB | 127.0.0.1 | 27017 | 数据库 open_bbs |
| Nacos | 127.0.0.1 | 8848 | 服务注册与发现 |

## 🚀 服务端口配置

当前项目各服务端口分配：

| 服务 | 模块 | 端口 | 说明 |
|------|------|------|------|
| bbs-rest | bbs-rest | 7012 | API 网关/聚合服务（您已修改） |
| bbs-article | bbs-article-service | 7011 | 文章服务 |
| bbs-user | bbs-user-service | 7013 | 用户服务 |

## ⚙️ 启动前检查清单

### 1. 确认中间件运行状态

```bash
# 检查 MySQL
mysql -h 127.0.0.1 -P 3306 -u root -p

# 检查 Redis
redis-cli -h 127.0.0.1 -p 6379 ping

# 检查 MongoDB
mongosh mongodb://127.0.0.1:27017/open_bbs

# 检查 Nacos
curl http://127.0.0.1:8848/nacos
```

### 2. 数据库准备

确保 MySQL 中已创建 `open_bbs` 数据库并导入表结构：

```sql
CREATE DATABASE IF NOT EXISTS open_bbs CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

导入 SQL 文件：
```bash
mysql -h 127.0.0.1 -P 3306 -u root -p open_bbs < db/open_bbs.sql
```

### 3. Nacos 配置

Nacos 使用默认配置即可，服务会自动注册。

访问 Nacos 控制台：http://127.0.0.1:8848/nacos
- 用户名：nacos
- 密码：nacos

## 🔧 配置文件说明

### bbs-rest (端口: 7012)
配置文件：`bbs-rest/src/main/resources/application.yml`

**注意**：bbs-rest 模块不需要配置数据库，它通过 Feign 调用其他服务。

### bbs-article (端口: 7011)
配置文件：`bbs-article/bbs-article-service/src/main/resources/application.yml`

需要配置：
- MySQL 连接（用户名/密码请根据实际情况修改）
- Redis 连接
- MongoDB 连接

### bbs-user (端口: 7013)
配置文件：`bbs-user/bbs-user-service/src/main/resources/application.yml`

需要配置：
- MySQL 连接（用户名/密码请根据实际情况修改）
- Redis 连接

## 🎯 启动顺序

推荐按以下顺序启动服务：

1. **启动所有中间件**（MySQL、Redis、MongoDB、Nacos）
2. **启动 bbs-user-service**（用户服务，端口 7013）
3. **启动 bbs-article-service**（文章服务，端口 7011）
4. **启动 bbs-rest**（API 服务，端口 7012）

## 💻 启动命令

### 方式1：使用 Maven 启动（推荐用于开发）

```bash
# 在项目根目录执行

# 1. 先编译打包
mvn clean install -DskipTests

# 2. 分别启动各服务（新终端窗口）
cd bbs-user/bbs-user-service
mvn spring-boot:run

cd bbs-article/bbs-article-service
mvn spring-boot:run

cd bbs-rest
mvn spring-boot:run
```

### 方式2：使用 IDE 启动

在 IDEA 中直接运行各模块的 Application 类：
- `UserApplication.java`
- `ArticleApplication.java`
- `RestApplication.java`

### 方式3：使用 JAR 包启动（推荐用于生产）

```bash
# 1. 打包
mvn clean package -DskipTests

# 2. 启动各服务
java -jar bbs-user/bbs-user-service/target/bbs-user-service.jar
java -jar bbs-article/bbs-article-service/target/bbs-article-service.jar
java -jar bbs-rest/target/bbs-rest.jar
```

## ✅ 验证服务

### 1. 检查 Nacos 服务注册

访问：http://127.0.0.1:8848/nacos

在"服务管理" -> "服务列表"中应该能看到：
- ns-bbs-rest
- ns-bbs-article
- ns-bbs-user

### 2. 访问 Swagger 文档

- **bbs-rest**: http://localhost:7012/api/doc.html
- **bbs-article**: http://localhost:7011/doc.html
- **bbs-user**: http://localhost:7013/doc.html

### 3. 测试接口

```bash
# 测试 bbs-rest 是否启动成功
curl http://localhost:7012/api/doc.html
```

## 🔍 常见问题

### Q1: 服务启动失败，提示连接 Nacos 失败

**原因**：Nacos 未启动或端口不对

**解决**：
```bash
# 检查 Nacos 是否运行
curl http://127.0.0.1:8848/nacos

# 如果未启动，启动 Nacos
cd /path/to/nacos/bin
startup.cmd -m standalone  # Windows
./startup.sh -m standalone  # Linux/Mac
```

### Q2: Feign 调用返回 503 错误

**原因**：目标服务未注册到 Nacos 或未启动

**解决**：
1. 检查目标服务是否已启动
2. 查看 Nacos 控制台确认服务已注册
3. 检查服务名称配置是否正确

### Q3: 数据库连接失败

**原因**：MySQL 未启动或用户名密码错误

**解决**：
1. 检查 MySQL 是否运行
2. 修改 `application.yml` 中的数据库用户名和密码
   ```yaml
   spring:
     datasource:
       username: your_username
       password: your_password
   ```

### Q4: Redis 连接失败

**原因**：Redis 未启动或需要密码

**解决**：
1. 检查 Redis 是否运行
2. 如果需要密码，在 `application.yml` 中配置：
   ```yaml
   spring:
     data:
       redis:
         password: your_password
   ```

## 📝 配置文件修改建议

如果您的中间件配置与默认不同，请修改以下配置：

### 1. MySQL 用户名和密码

在 `bbs-article-service` 和 `bbs-user-service` 的 `application.yml` 中：

```yaml
spring:
  datasource:
    username: your_username  # 改为你的 MySQL 用户名
    password: your_password  # 改为你的 MySQL 密码
```

### 2. Redis 密码（如果有）

在所有模块的 `application.yml` 中：

```yaml
spring:
  data:
    redis:
      password: your_redis_password  # 如果 Redis 有密码
```

### 3. MongoDB 认证（如果有）

在 `bbs-article-service` 的 `application.yml` 中：

```yaml
spring:
  data:
    mongodb:
      uri: mongodb://username:password@127.0.0.1:27017/open_bbs
```

## 🎨 前端配置

如果您还需要启动前端项目：

前端项目路径：`Front/bbs-vue3-ui`

修改前端 API 配置（通常在 `src/config/config.js` 或环境变量文件中）：

```javascript
// 将 API 地址指向本地
export const API_BASE_URL = 'http://localhost:7012/api';
```

启动前端：
```bash
cd Front/bbs-vue3-ui
npm install
npm run serve
```

## 📊 服务依赖关系

```
bbs-rest (7012)
  ├── Feign → bbs-article (7011)
  ├── Feign → bbs-user (7013)
  └── Feign → ns-manage-auth (外部认证服务，可选)

bbs-article (7011)
  ├── MySQL (127.0.0.1:3306)
  ├── MongoDB (127.0.0.1:27017)
  ├── Redis (127.0.0.1:6379)
  └── Nacos (127.0.0.1:8848)

bbs-user (7013)
  ├── MySQL (127.0.0.1:3306)
  ├── Redis (127.0.0.1:6379)
  └── Nacos (127.0.0.1:8848)
```

## 🔐 外部依赖：ns-manage-auth

项目依赖 `ns-manage-auth` 认证服务（南生运营系统）。如果本地没有该服务：

**选项1**：启动完整的南生运营系统（推荐）
**选项2**：修改代码，暂时禁用需要认证的功能

## 📞 技术支持

如遇到问题，请检查：
1. 日志文件：`logs/bbs-*/` 目录
2. Nacos 控制台服务状态
3. 各服务启动日志

---

**祝开发顺利！** 🎉
