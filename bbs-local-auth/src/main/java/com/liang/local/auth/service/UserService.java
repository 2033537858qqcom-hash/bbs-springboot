package com.liang.local.auth.service;

import com.liang.local.auth.entity.User;
import com.liang.manage.auth.facade.dto.user.UserTokenDTO;
import java.util.List;
import java.util.Map;

/**
 * 用户服务接口
 */
public interface UserService {
    
    /**
     * 根据ID获取用户
     */
    User getById(Long id);
    
    /**
     * 批量获取用户
     */
    List<User> getByIds(List<Long> userIds);
    
    /**
     * 根据用户名获取用户
     */
    User getByUsername(String username);
    
    /**
     * 用户登录
     */
    Map<String, Object> login(String username, String password);
    
    /**
     * 用户注册
     */
    Map<String, Object> register(String username, String email, String password);
    
    /**
     * 验证用户名是否存在
     */
    boolean existsByUsername(String username);
    
    /**
     * 验证邮箱是否存在
     */
    boolean existsByEmail(String email);
    
    /**
     * 获取所有用户列表
     */
    List<User> getAllList();
}
