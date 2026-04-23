package com.liang.local.auth.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.liang.local.auth.entity.User;
import com.liang.local.auth.mapper.UserMapper;
import com.liang.local.auth.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 用户服务实现类
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private StringRedisTemplate redisTemplate;
    
    @Value("${jwt.secret}")
    private String jwtSecret;
    
    @Value("${jwt.expiration}")
    private Long jwtExpiration;
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    // Redis Key 前缀
    private static final String USER_CACHE_KEY = "local:auth:user:";
    private static final String TOKEN_CACHE_KEY = "local:auth:token:";
    private static final long CACHE_EXPIRE_HOURS = 24;

    @Override
    public User getById(Long id) {
        if (id == null) {
            return null;
        }
        
        // 先从Redis缓存获取
        String cacheKey = USER_CACHE_KEY + id;
        String cacheJson = redisTemplate.opsForValue().get(cacheKey);
        if (StringUtils.isNotBlank(cacheJson)) {
            log.debug("从缓存获取用户信息: {}", id);
            // 简化实现，实际应使用JSON反序列化
            return userMapper.getById(id);
        }
        
        // 从数据库获取
        User user = userMapper.getById(id);
        if (user != null) {
            // 存入缓存
            redisTemplate.opsForValue().set(cacheKey, String.valueOf(user.getId()), 
                    CACHE_EXPIRE_HOURS, TimeUnit.HOURS);
        }
        
        return user;
    }

    @Override
    public List<User> getByIds(List<Long> userIds) {
        if (userIds == null || userIds.isEmpty()) {
            return Collections.emptyList();
        }
        
        // 去重
        List<Long> distinctIds = new ArrayList<>(new HashSet<>(userIds));
        
        // 批量查询
        return userMapper.getByIds(distinctIds);
    }

    @Override
    public User getByUsername(String username) {
        if (StringUtils.isBlank(username)) {
            return null;
        }
        return userMapper.getByUsername(username);
    }

    @Override
    public Map<String, Object> login(String username, String password) {
        log.info("用户登录: {}", username);
        
        // 参数校验
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            throw new RuntimeException("用户名和密码不能为空");
        }
        
        // 查询用户
        User user = userMapper.getByUsername(username);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        
        // 验证密码
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("密码错误");
        }
        
        // 检查用户状态
        if (!user.getState()) {
            throw new RuntimeException("用户已被禁用");
        }
        
        // 生成Token
        String token = generateToken(user.getId(), user.getUsername());
        
        // 将Token存入Redis
        String tokenKey = TOKEN_CACHE_KEY + token;
        redisTemplate.opsForValue().set(tokenKey, String.valueOf(user.getId()), 
                jwtExpiration, TimeUnit.MILLISECONDS);
        
        // 返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("userId", user.getId());
        result.put("token", token);
        
        return result;
    }

    @Override
    public Map<String, Object> register(String username, String email, String password) {
        log.info("用户注册: username={}, email={}", username, email);
        
        // 参数校验
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            throw new RuntimeException("用户名和密码不能为空");
        }
        
        // 检查用户名是否存在
        if (userMapper.countByUsername(username) > 0) {
            throw new RuntimeException("用户名已存在");
        }
        
        // 检查邮箱
        if (StringUtils.isNotBlank(email) && userMapper.countByEmail(email) > 0) {
            throw new RuntimeException("邮箱已被注册");
        }
        
        // 创建用户
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setState(true);
        
        // 插入数据库
        userMapper.insert(user);
        log.info("用户注册成功: id={}, username={}", user.getId(), username);
        
        // 生成Token
        String token = generateToken(user.getId(), user.getUsername());
        
        // 将Token存入Redis
        String tokenKey = TOKEN_CACHE_KEY + token;
        redisTemplate.opsForValue().set(tokenKey, String.valueOf(user.getId()), 
                jwtExpiration, TimeUnit.MILLISECONDS);
        
        // 返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("userId", user.getId());
        result.put("token", token);
        
        return result;
    }

    @Override
    public boolean existsByUsername(String username) {
        return userMapper.countByUsername(username) > 0;
    }

    @Override
    public boolean existsByEmail(String email) {
        return StringUtils.isNotBlank(email) && userMapper.countByEmail(email) > 0;
    }
    
    @Override
    public List<User> getAllList() {
        log.info("获取所有用户列表");
        return userMapper.getAllList();
    }
    
    /**
     * 验证Token
     */
    public boolean verifyToken(String token) {
        if (StringUtils.isBlank(token)) {
            return false;
        }
        
        try {
            // 验证JWT签名和过期时间
            JWT.require(Algorithm.HMAC256(jwtSecret)).build().verify(token);
            
            // 检查Token是否在Redis中
            String tokenKey = TOKEN_CACHE_KEY + token;
            return redisTemplate.hasKey(tokenKey);
        } catch (Exception e) {
            log.warn("Token验证失败: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * 根据Token获取用户ID
     */
    public Long getUserIdByToken(String token) {
        if (!verifyToken(token)) {
            return null;
        }
        
        try {
            // 从Redis获取用户ID
            String tokenKey = TOKEN_CACHE_KEY + token;
            String userIdStr = redisTemplate.opsForValue().get(tokenKey);
            return StringUtils.isNotBlank(userIdStr) ? Long.parseLong(userIdStr) : null;
        } catch (Exception e) {
            log.error("从Token获取用户ID失败: {}", e.getMessage());
            return null;
        }
    }
    
    /**
     * 登出
     */
    public boolean logout(String token) {
        if (StringUtils.isBlank(token)) {
            return false;
        }
        
        try {
            // 从Redis删除Token
            String tokenKey = TOKEN_CACHE_KEY + token;
            redisTemplate.delete(tokenKey);
            log.info("用户登出成功");
            return true;
        } catch (Exception e) {
            log.error("用户登出失败: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * 生成JWT Token
     */
    private String generateToken(Long userId, String username) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);
        
        return JWT.create()
                .withSubject(String.valueOf(userId))
                .withClaim("userId", userId)
                .withClaim("username", username)
                .withIssuedAt(now)
                .withExpiresAt(expiryDate)
                .sign(Algorithm.HMAC256(jwtSecret));
    }
}
