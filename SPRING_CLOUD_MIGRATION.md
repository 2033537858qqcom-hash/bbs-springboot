# Spring Cloud Migration

## 已完成骨架调整

- 父 `pom` 已移除 `Dubbo + Zookeeper` 依赖，改为 `OpenFeign + Nacos Discovery`
- `bbs-rest`、`bbs-article-service`、`bbs-user-service` 的 `application.yml` 已移除 `dubbo` 配置，并补充 `spring.cloud.nacos.discovery.server-addr`
- `RestApplication`、`ArticleApplication`、`UserApplication` 已启用注册发现能力
- `GlobalExceptionHandler` 已从 `RpcException` 切换为 `FeignException`
- 大部分 `@DubboReference` 已替换为 Spring 注入方式
- 已清理一批由批量替换产生的 BOM/乱码语法问题
- `@EnableFeignClients` 扫描范围已收紧，避免后续 Feign 客户端与本地实现冲突

## 当前未完成的关键迁移

### 1. 内部 BBS 服务调用

当前内部主链路已经按“`controller/internal` + `FeignClient`”模式完成迁移，已覆盖：

- `bbs-rest -> article-service`
- `bbs-rest -> user-service`
- `bbs-article-service -> user-service`
- `bbs-user-service -> article-service`

已完成迁移的核心接口包括：

- `ArticleService`
- `CommentService`
- `LabelService`
- `ResourceNavigateService`
- `SlideshowService`
- `UserLevelService`
- `LikeService`
- `LikeCommentService`
- `FollowService`
- `DynamicService`

当前阶段内部调用侧重点已经从“补链路”转为“静态校验 + 最终联调验证”。

### 2. 外部认证/文件服务调用

当前还依赖外部 `com.liang.manage.auth.facade.server.*` 接口：

- `UserService`
- `FileService`
- `VisitService`
- `NotifyService`
- `UrlAccessRightService`

这部分原先大概率也是通过 Dubbo 远程注入，后续也需要同步迁移到 Spring Cloud。

当前已完成两层收口：

- 各模块已提供本地 `FeignClient` 入口，例如 `UserServiceClient`、`FileServiceClient`、`VisitServiceClient`
- 业务类、控制器、拦截器已改为显式注入各自模块下的 `*Client`，不再直接注入远端接口类型

### 3. 最终验证

尚未执行完整编译与联调验证。

当前仍建议保持你之前确认的节奏：

- 先继续做代码层改造与静态诊断
- 最后再统一做 `mvn clean compile` / 本地联调

## 推荐的后续改造顺序

### 第一批

- 先迁移 `SlideshowService`
- 再迁移 `LabelService`
- 再迁移 `ResourceNavigateService`

原因：

- 参数简单
- 无复杂文件组合参数
- 改完后能先验证最小链路

### 第二批

- 迁移 `CommentService`
- 迁移 `DynamicService`
- 迁移 `FollowService`
- 迁移 `LikeService`
- 迁移 `LikeCommentService`

### 第三批

- 迁移 `ArticleService`
- 迁移 `UserLevelService`

原因：

- 方法最多
- DTO 组合复杂
- 涉及分页、当前用户、统计聚合和文件上传

### 第四批

- 迁移外部 `manage-auth` 系列接口
- 统一补齐 Feign 超时、重试、异常处理与日志

## 建议的目录结构

建议在各模块新增如下目录：

- `bbs-rest/src/main/java/.../client`
- `bbs-article-service/src/main/java/.../client`
- `bbs-user-service/src/main/java/.../client`
- `bbs-article-service/src/main/java/.../controller/internal`
- `bbs-user-service/src/main/java/.../controller/internal`

## 当前状态说明

当前代码处于“迁移骨架已建立，内部 BBS 主链路已基本切到 Feign，本地资源配置已完成”的阶段。

也就是说：

- Dubbo / Zookeeper 基础依赖已开始拆除
- Spring Cloud / Nacos 基础设施已接入
- 内部 BBS 服务调用主链路已经补成 Feign 客户端与 HTTP 提供端
- 仍待最终编译验证与本地联调确认

### 已打通的第一批内部链路

- `bbs-rest -> ns-bbs-article -> SlideshowService`
- `bbs-rest -> ns-bbs-article -> LabelService`
- `bbs-rest -> ns-bbs-article -> ResourceNavigateService`

这三条链路当前已经补齐：

- `bbs-rest` Feign 客户端
- `bbs-article-service` 内部 HTTP 提供端
- 本地 Nacos 服务名配置 `local.services.bbs-article.name=ns-bbs-article`

### 已打通的第二批内部链路

- `bbs-rest -> ns-bbs-article -> CommentService`
- `bbs-rest -> ns-bbs-user -> DynamicService`
- `bbs-rest -> ns-bbs-user -> FollowService`
- `bbs-rest -> ns-bbs-user -> LikeService`
- `bbs-rest -> ns-bbs-user -> LikeCommentService`

同时补齐了这批链路涉及的服务间依赖：

- `ns-bbs-article -> ns-bbs-user`：
  - `CommentService -> UserLevelService`
  - `CommentService -> LikeCommentService`
- `ns-bbs-user -> ns-bbs-article`：
  - `DynamicService -> ArticleService / CommentService`
  - `FollowService -> ArticleService`
  - `LikeService -> ArticleService`
  - `UserLevelService -> ArticleService`

### 已打通的第三批内部链路

- `bbs-rest -> ns-bbs-article -> ArticleService`
- `bbs-rest -> ns-bbs-user -> UserLevelService`

同时补齐了这批链路涉及的剩余服务间依赖：

- `ns-bbs-article -> ns-bbs-user`：
  - `ArticleService -> LikeService`
  - `ArticleService -> FollowService`
  - `ArticleService -> UserLevelService`

并完成以下入口切换：

- `ArticleController` 改为通过 `ArticleArticleClient` 调用文章服务
- `UserController` 改为通过 `UserLevelClient` / `ArticleArticleClient` 调用用户等级与文章统计
- `LoginController` 注册后改为通过 `UserLevelClient` 初始化用户等级

## 本地运行约定

当前工程已经按“本地资源、本地注册中心”的方式调整：

- `MySQL` 使用本机 `127.0.0.1:3306/open_bbs`
- `Redis` 使用本机 `127.0.0.1:6379`
- `MongoDB` 使用本机 `127.0.0.1:27017/open_bbs`
- `Nacos` 使用本机 `127.0.0.1:8848`

同时，外部 `manage-auth` 相关接口已经补了 Feign 客户端入口，默认注册服务名为：

- `ns-manage-auth`

如果你本地实际启动的认证服务名不是这个值，只需要修改各模块 `application.yml` 中的：

- `local.services.manage-auth.name`
