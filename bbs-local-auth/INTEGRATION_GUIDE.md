# BBS 本地认证服务详细对接文档

## 📋 目录

1. [服务架构](#服务架构)
2. [接口清单](#接口清单)
3. [接口详细说明](#接口详细说明)
4. [数据格式](#数据格式)
5. [调用示例](#调用示例)
6. [常见问题](#常见问题)

---

## 服务架构

### 整体架构图

```
┌─────────────────────────────────────────────────────────┐
│                    前端 (Vue3)                           │
│              http://localhost:8080                       │
└────────────────────┬────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────┐
│              bbs-rest (7012)                             │
│         API 网关 / 聚合服务                               │
│  - 登录拦截器 (LoginInterceptor)                         │
│  - 访问记录拦截器 (VisitInterceptor)                     │
│  - 通知拦截器 (NotifyInterceptor)                        │
│  - URL权限拦截器 (UrlAccessCheckInterceptor)             │
└────────────────────┬────────────────────────────────────┘
                     │ Feign 调用
                     ▼
┌─────────────────────────────────────────────────────────┐
│          bbs-local-auth (7014) ← 本地认证服务             │
│  替代原来的 ns-manage-auth                              │
│                                                          │
│  提供：                                                   │
│  - 用户认证 (登录/注册/Token验证)                         │
│  - 用户信息查询                                          │
│  - 访问记录统计                                          │
│  - 通知管理                                              │
│  - URL权限检查                                           │
└─────────────────────────────────────────────────────────┘
```

### 服务注册

在 Nacos 中注册为：
- **服务名**: `ns-manage-auth`
- **地址**: `http://127.0.0.1:7014`
- **健康状态**: UP

---

## 接口清单

### 1. 用户认证模块 (/user)

| 接口 | 方法 | 路径 | 说明 |
|------|------|------|------|
| 登录 | POST | `/user/login` | 用户登录获取Token |
| 注册 | POST | `/user/register` | 用户注册 |
| 登出 | GET | `/user/logout` | 用户登出 |
| 验证Token | GET | `/user/verify-token` | 验证Token有效性 |
| Token是否过期 | GET | `/user/is-expired` | 检查Token是否过期 |
| 获取Token用户 | GET | `/user/token-user` | 根据Token获取用户信息 |
| 内部登录URL | GET | `/user/inner-login-url` | 获取内部登录页面URL |
| 批量获取用户 | GET | `/user/by-ids` | 根据ID列表批量获取用户 |

### 2. 访问记录模块 (/visit)

| 接口 | 方法 | 路径 | 说明 |
|------|------|------|------|
| 创建访问记录 | POST | `/visit/create` | 记录用户访问 |
| 获取总访问量 | GET | `/visit/total` | 获取总访问次数 |

### 3. 通知模块 (/notify)

| 接口 | 方法 | 路径 | 说明 |
|------|------|------|------|
| 标记已读 | POST | `/notify/have-read` | 标记通知已读 |
| 批量标记已读 | POST | `/notify/mark-read` | 批量标记通知 |
| 获取通知列表 | GET | `/notify/list` | 获取通知列表 |
| 未读数量 | GET | `/notify/not-read-count` | 获取未读通知数量 |

### 4. 权限模块 (/url-access-right)

| 接口 | 方法 | 路径 | 说明 |
|------|------|------|------|
| 检查URL权限 | GET | `/url-access-right/check` | 检查用户是否有权限访问某URL |

---

## 接口详细说明

### 1. 用户登录

**接口**: `POST /user/login`

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
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**调用位置**:
- 前端登录页面
- `bbs-rest` 的 `LoginController`

---

### 2. 用户注册

**接口**: `POST /user/register`

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
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

---

### 3. 验证Token

**接口**: `GET /user/verify-token`

**请求参数**:
```
token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**响应**:
```json
true  // 或 false
```

**调用位置**:
- `LoginInterceptor.preHandle()` - 每次请求都会验证

---

### 4. 获取Token用户信息

**接口**: `GET /user/token-user`

**请求参数**:
```
token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

**响应**:
```json
{
  "userId": 1640,
  "username": "admin"
}
```

**调用位置**:
- `LoginInterceptor.preHandle()` - 获取当前登录用户

---

### 5. 批量获取用户信息

**接口**: `GET /user/by-ids`

**请求参数**:
```
userIds=1812&userIds=1640&userIds=1234
```

**响应**:
```json
[
  {
    "id": 1812,
    "username": "user_1812"
  },
  {
    "id": 1640,
    "username": "user_1640"
  }
]
```

**调用位置**:
- `ArticleServiceImpl.buildArticleInfo()` - 获取文章作者信息
- 文章列表、评论列表等需要显示用户信息的地方

---

### 6. 创建访问记录

**接口**: `POST /visit/create`

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

**调用位置**:
- `VisitInterceptor.preHandle()` - 每次请求自动记录

---

### 7. 获取未读通知数量

**接口**: `GET /notify/not-read-count`

**请求参数**:
```
userId=1640&type=1
```

**响应**:
```json
0
```

**调用位置**:
- `NotifyInterceptor.preHandle()` - 在响应头中添加未读数量

---

### 8. 检查URL权限

**接口**: `GET /url-access-right/check`

**请求参数**:
```
uri=/api/bbs/article/create&attribute={}
```

**响应**:
```json
true  // 本地开发环境默认返回true
```

**调用位置**:
- `UrlAccessCheckInterceptor.preHandle()` - 检查用户权限

---

## 数据格式

### UserSsoDTO (用户SSO信息)

```java
public class UserSsoDTO {
    private Long userId;        // 用户ID
    // 注意：当前版本没有username字段
}
```

### 响应格式

**成功响应**: 直接返回数据或 `true`

**失败响应**: 返回 `false` 或抛出异常

---

## 调用示例

### 前端登录流程

```javascript
// 1. 用户登录
const loginResponse = await fetch('http://localhost:7012/api/bbs/sso/login', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({
    username: 'admin',
    password: '123456'
  })
});

const loginData = await loginResponse.json();
const token = loginData.token;

// 2. 保存Token到Cookie
document.cookie = `NS_ACCOUNT_SSO_COOKIE=${token}; path=/; domain=localhost.com`;

// 3. 后续请求自动携带Cookie
const articles = await fetch('http://localhost:7012/api/bbs/article/getList', {
  credentials: 'include'  // 自动携带Cookie
});
```

### Feign 客户端调用

```java
// bbs-rest 中的 UserServiceClient
@FeignClient(
    contextId = "restUserServiceClient",
    name = "${local.services.manage-auth.name:ns-manage-auth}",
    path = "/user"
)
public interface UserServiceClient {
    
    @GetMapping("/verify-token")
    Boolean verifyToken(@RequestParam("token") String token);
    
    @GetMapping("/token-user")
    UserSsoDTO getUserSsoDTOByToken(@RequestParam("token") String token);
    
    @GetMapping("/by-ids")
    List<Map<String, Object>> getByIds(@RequestParam("userIds") List<Long> userIds);
}
```

### 拦截器使用示例

```java
// LoginInterceptor.java
@Override
public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    // 1. 从Cookie获取Token
    Cookie ssoAccount = HttpRequestUtils.findCookie(request, "NS_ACCOUNT_SSO_COOKIE");
    
    if (ssoAccount != null) {
        String token = ssoAccount.getValue();
        
        // 2. 调用本地认证服务验证Token
        Boolean isValid = userService.verifyToken(token);
        
        if (isValid) {
            // 3. 获取用户信息
            UserSsoDTO currentUser = userService.getUserSsoDTOByToken(token);
            UserContextUtils.setCurrentUser(currentUser);
        }
    }
    
    return true;
}
```

---

## 常见问题

### Q1: 404 Not Found

**问题**: 调用接口返回 404

**原因**: 
- `bbs-local-auth` 服务未启动
- 接口路径错误

**解决**:
```bash
# 检查服务是否运行
curl http://localhost:7014/user/verify-token?token=test

# 查看 Nacos 服务列表
http://localhost:8848/nacos
```

---

### Q2: Token 验证失败

**问题**: `verifyToken` 返回 `false`

**原因**:
- Token 未生成或已过期
- Token 未存储到 `tokenStore`

**解决**:
1. 确保先调用 `/user/login` 或 `/user/register` 获取Token
2. 检查Token是否正确传递

---

### Q3: 用户信息为空

**问题**: `token-user` 返回 `null`

**原因**:
- Token 无效
- Token 已登出

**解决**:
```bash
# 重新登录获取新Token
curl -X POST http://localhost:7014/user/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"123456"}'
```

---

### Q4: 批量获取用户信息失败

**问题**: `by-ids` 返回空列表或错误

**原因**:
- 参数格式错误
- userIds 为空

**解决**:
```bash
# 正确的参数格式
curl "http://localhost:7014/user/by-ids?userIds=1812&userIds=1640"
```

---

### Q5: Feign 调用超时

**问题**: Feign 调用超时异常

**解决**: 在 `application.yml` 中增加超时时间
```yaml
feign:
  client:
    config:
      default:
        connectTimeout: 5000
        readTimeout: 10000
```

---

## JWT Token 说明

### Token 生成

```java
private String generateToken(Long userId, String username) {
    Date now = new Date();
    Date expiryDate = new Date(now.getTime() + 86400000); // 24小时
    
    return JWT.create()
            .withSubject(String.valueOf(userId))
            .withClaim("userId", userId)
            .withClaim("username", username)
            .withIssuedAt(now)
            .withExpiresAt(expiryDate)
            .sign(Algorithm.HMAC256("bbs-local-auth-secret-key-2024"));
}
```

### Token 验证

```java
JWT.require(Algorithm.HMAC256("bbs-local-auth-secret-key-2024"))
   .build()
   .verify(token);
```

### Token 结构

```
Header:  {"alg": "HS256", "typ": "JWT"}
Payload: {"sub": "1640", "userId": 1640, "username": "admin", "iat": 1234567890, "exp": 1234654290}
Signature: HMACSHA256(...)
```

---

## 数据存储说明

### 当前实现（内存存储）

```java
// Token 存储
private static Map<String, UserSsoDTO> tokenStore = new ConcurrentHashMap<>();
```

**特点**:
- ✅ 快速启动，无需数据库
- ✅ 适合开发测试
- ⚠️ 重启后数据丢失
- ⚠️ 不支持多实例部署

### 生产环境建议

如需持久化，建议：

1. **使用 Redis 存储 Token**
```java
@Autowired
private StringRedisTemplate redisTemplate;

// 存储Token
redisTemplate.opsForValue().set("token:" + token, userJson, 24, TimeUnit.HOURS);

// 获取Token
String userJson = redisTemplate.opsForValue().get("token:" + token);
```

2. **使用 MySQL 存储用户信息**
```java
@Mapper
public interface UserMapper {
    @Select("SELECT * FROM user WHERE id = #{id}")
    UserDTO getById(Long id);
}
```

---

## 扩展开发

### 添加新接口

1. 在 `LocalAuthController` 中添加方法
2. 使用 `@GetMapping` 或 `@PostMapping` 注解
3. 重启服务即可

### 示例：添加获取用户详情

```java
@GetMapping("/detail")
public Map<String, Object> getUserDetail(@RequestParam("userId") Long userId) {
    Map<String, Object> user = new HashMap<>();
    user.put("id", userId);
    user.put("username", "user_" + userId);
    user.put("email", "user_" + userId + "@example.com");
    user.put("avatar", "/avatars/default.png");
    return user;
}
```

---

## 调试技巧

### 1. 查看服务日志

```bash
# 实时查看日志
tail -f logs/bbs-local-auth/bbs-local-auth-info.log

# 查看错误日志
tail -f logs/bbs-local-auth/bbs-local-auth-error.log
```

### 2. 使用 Postman 测试

导入接口集合，直接测试各个接口。

### 3. 开启 Debug 日志

在 `application.yml` 中添加：
```yaml
logging:
  level:
    com.liang.local.auth: DEBUG
```

### 4. 查看 Nacos 服务状态

访问：http://localhost:8848/nacos
- 服务名：ns-manage-auth
- 健康实例数：1

---

## 性能优化建议

### 1. 添加缓存

```java
// 使用 Caffeine 本地缓存
private Cache<Long, Map<String, Object>> userCache = Caffeine.newBuilder()
    .maximumSize(1000)
    .expireAfterWrite(10, TimeUnit.MINUTES)
    .build();
```

### 2. 批量查询优化

```java
// 减少循环，使用流式处理
@GetMapping("/by-ids")
public List<Map<String, Object>> getByIds(@RequestParam("userIds") List<Long> userIds) {
    return userIds.stream()
        .distinct()
        .map(this::buildUserInfo)
        .collect(Collectors.toList());
}
```

---

## 总结

`bbs-local-auth` 完全替代了原来的 `ns-manage-auth`，提供了：

✅ **完整的认证流程** - 登录、注册、Token验证  
✅ **用户信息查询** - 单个/批量获取用户信息  
✅ **访问记录** - 自动记录访问统计  
✅ **通知系统** - 通知管理（简化版）  
✅ **权限检查** - URL级别权限控制  
✅ **零外部依赖** - 完全本地化运行  

现在您可以在完全独立的环境中开发和测试 BBS 项目了！🎉
