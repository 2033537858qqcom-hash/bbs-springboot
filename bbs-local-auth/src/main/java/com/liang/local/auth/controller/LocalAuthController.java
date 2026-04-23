package com.liang.local.auth.controller;

import com.liang.local.auth.entity.User;
import com.liang.local.auth.service.UserService;
import com.liang.local.auth.service.impl.UserServiceImpl;
import com.liang.manage.auth.facade.dto.user.UserDTO;
import com.liang.manage.auth.facade.dto.user.UserListDTO;
import com.liang.nansheng.common.auth.UserSsoDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 本地认证服务Controller - 完整实现版
 * 替代原 ns-manage-auth 服务
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class LocalAuthController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private UserServiceImpl userServiceImpl;

    /**
     * 根据ID列表批量获取用户信息
     */
    @GetMapping("/by-ids")
    public List<UserDTO> getByIds(@RequestParam("userIds") List<Long> userIds) {
        log.info("批量获取用户信息: {}", userIds);
        List<User> users = userService.getByIds(userIds);
        return users.stream()
                .map(this::convertToUserDTO)
                .collect(Collectors.toList());
    }

    /**
     * 获取所有用户列表
     */
    @GetMapping("/all-list")
    public List<UserListDTO> getAllList() {
        log.info("获取所有用户列表");
        List<User> users = userService.getAllList();
        return users.stream()
                .map(this::convertToUserListDTO)
                .collect(Collectors.toList());
    }

    /**
     * 获取内部登录URL
     */
    @GetMapping("/inner-login-url")
    public String innerLoginUrl(@RequestParam("referer") String referer) {
        log.info("innerLoginUrl called with referer: {}", referer);
        return "http://localhost:8080/login?redirect=" + referer;
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> loginRequest) {
        log.info("用户登录: {}", loginRequest.get("username"));
        
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");
        
        return userService.login(username, password);
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody Map<String, String> registerRequest) {
        log.info("用户注册: {}", registerRequest.get("username"));
        
        String username = registerRequest.get("username");
        String email = registerRequest.get("email");
        String password = registerRequest.get("password");
        
        return userService.register(username, email, password);
    }

    /**
     * 用户登出
     */
    @GetMapping("/logout")
    public Boolean logout(@RequestParam("token") String token) {
        log.info("用户登出");
        return userServiceImpl.logout(token);
    }

    /**
     * 验证Token
     */
    @GetMapping("/verify-token")
    public Boolean verifyToken(@RequestParam("token") String token) {
        return userServiceImpl.verifyToken(token);
    }

    /**
     * 检查Token是否过期
     */
    @GetMapping("/is-expired")
    public Boolean isTokenExpired(@RequestParam("token") String token) {
        return !userServiceImpl.verifyToken(token);
    }

    /**
     * 根据Token获取用户信息
     */
    @GetMapping("/token-user")
    public UserSsoDTO getUserByToken(@RequestParam("token") String token) {
        if (StringUtils.isBlank(token)) {
            return null;
        }
        
        Long userId = userServiceImpl.getUserIdByToken(token);
        if (userId == null) {
            return null;
        }
        
        UserSsoDTO ssoDTO = new UserSsoDTO();
        ssoDTO.setUserId(userId);
        
        return ssoDTO;
    }

    /**
     * 根据ID获取用户信息
     */
    @GetMapping("/get")
    public User getById(@RequestParam("id") Long id) {
        return userService.getById(id);
    }

    /**
     * 检查邮箱是否存在
     */
    @GetMapping("/email")
    public Boolean existsByEmail(@RequestParam("email") String email) {
        return userService.existsByEmail(email);
    }

    /**
     * 转换 User 到 UserDTO
     */
    private UserDTO convertToUserDTO(User user) {
        if (user == null) {
            return null;
        }
        UserDTO dto = new UserDTO();
        BeanUtils.copyProperties(user, dto);
        return dto;
    }

    /**
     * 转换 User 到 UserListDTO
     */
    private UserListDTO convertToUserListDTO(User user) {
        if (user == null) {
            return null;
        }
        UserListDTO dto = new UserListDTO();
        BeanUtils.copyProperties(user, dto);
        return dto;
    }
}
