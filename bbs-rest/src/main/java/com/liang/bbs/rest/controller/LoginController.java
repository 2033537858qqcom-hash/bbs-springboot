package com.liang.bbs.rest.controller;

import com.liang.bbs.rest.client.UserServiceClient;
import com.liang.bbs.rest.client.UserLevelClient;
import com.liang.bbs.rest.config.login.NoNeedLogin;
import com.liang.bbs.rest.config.swagger.ApiVersion;
import com.liang.bbs.rest.config.swagger.ApiVersionConstant;
import com.liang.bbs.rest.utils.HttpRequestUtils;
import com.liang.bbs.user.facade.dto.InternalUserLevelCreateRequest;
import com.liang.manage.auth.facade.dto.user.UserDTO;
import com.liang.manage.auth.facade.dto.user.UserLoginDTO;
import com.liang.manage.auth.facade.dto.user.UserTokenDTO;
import com.liang.nansheng.common.constant.AuthSystemConstants;
import com.liang.nansheng.common.constant.TimeoutConstants;
import com.liang.nansheng.common.enums.ResponseCode;
import com.liang.nansheng.common.web.basic.ResponseResult;
import com.liang.nansheng.common.web.exception.BusinessException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.*;

/**
 */
@Slf4j
@RestController
@RequestMapping("/bbs/sso/")
@Tag(name = "閻劍鍩涚紒鐔剁閻ц缍嶉幒銉ュ經")
@CrossOrigin(origins = "*")
public class LoginController {
    @Autowired
    private UserServiceClient userService;

    @Autowired
    private UserLevelClient userLevelClient;

    @Value("${cookie.domain:}")
    private String domain;

    @NoNeedLogin
    @PostMapping("register")
    @Operation(summary = "用户注册")
    @ApiVersion(group = ApiVersionConstant.V_300)
    public ResponseResult<UserTokenDTO> register(@RequestBody UserDTO userDTO, HttpServletResponse response) {
        UserTokenDTO userTokenDTO = userService.register(userDTO);
        // 写入cookie
        addCookie(userTokenDTO.getToken(), response);
        // 初始化用户积分数据
        InternalUserLevelCreateRequest request = new InternalUserLevelCreateRequest();
        request.setUserId(userTokenDTO.getUserId());
        userLevelClient.create(request);
        return ResponseResult.success(userTokenDTO);
    }

    @NoNeedLogin
    @PostMapping("login")
    @Operation(summary = "用户登录")
    @ApiVersion(group = ApiVersionConstant.V_300)
    public ResponseResult<UserTokenDTO> login(@RequestBody UserLoginDTO userLoginDTO, HttpServletResponse response) {
        UserTokenDTO userTokenDTO = userService.login(userLoginDTO);
        // 写入cookie
        addCookie(userTokenDTO.getToken(), response);
        return ResponseResult.success(userTokenDTO);
    }

    @NoNeedLogin
    @GetMapping("logout")
    @Operation(summary = "用户登出")
    @ApiVersion(group = ApiVersionConstant.V_300)
    public ResponseResult<Boolean> logout(HttpServletRequest request, HttpServletResponse response) {
        try {
            // 获取cookie中的token
            Cookie ssoAccount = HttpRequestUtils.findCookie(request, AuthSystemConstants.NS_ACCOUNT_SSO_COOKIE);
            if (ssoAccount != null) {
                userService.logout(ssoAccount.getValue());
            }
            // 清除cookie
            clearCookie(response);
        } catch (Exception e) {
            throw BusinessException.build(ResponseCode.OPERATE_FAIL, "登出失败");
        }

        return ResponseResult.success(true);
    }

    /**
     * 写入cookie
     *
     * @param token
     * @param response
     */
    private void addCookie(String token, HttpServletResponse response) {
        // 创建Cookie, 设置名称和对应的值
        ResponseCookie.ResponseCookieBuilder cookieBuilder = ResponseCookie.from(AuthSystemConstants.NS_ACCOUNT_SSO_COOKIE, token)
                .maxAge(TimeoutConstants.NS_SSO_TIMEOUT)
                .path("/")
                .httpOnly(true);
//                .secure(true)
//                .sameSite("None");
        if (StringUtils.isNotBlank(domain)) {
            cookieBuilder.domain(domain);
        }
        ResponseCookie cookie = cookieBuilder.build();
        response.addHeader("Set-Cookie", cookie.toString());
    }

    /**
     * 清除cookie
     *
     * @param response
     */
    private void clearCookie(HttpServletResponse response) {
        // 创建Cookie, 设置名称和对应的值
        ResponseCookie.ResponseCookieBuilder cookieBuilder = ResponseCookie.from(AuthSystemConstants.NS_ACCOUNT_SSO_COOKIE, "")
                .maxAge(0)
                .path("/")
                .httpOnly(true);
//                .secure(true)
//                .sameSite("None");
        if (StringUtils.isNotBlank(domain)) {
            cookieBuilder.domain(domain);
        }
        ResponseCookie cookie = cookieBuilder.build();
        response.addHeader("Set-Cookie", cookie.toString());
    }

}

