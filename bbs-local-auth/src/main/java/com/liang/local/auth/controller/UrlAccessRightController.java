package com.liang.local.auth.controller;

import com.liang.nansheng.common.auth.UserSsoDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 本地URL权限检查Controller
 */
@Slf4j
@RestController
@RequestMapping("/url-access-right")
public class UrlAccessRightController {

    /**
     * 检查URL访问权限
     * 本地开发环境默认放行所有权限
     */
    @GetMapping("/check")
    public Boolean checkUrlAccess(@RequestHeader(value = "X-Current-User", required = false) String currentUserJson,
                                  @RequestParam("uri") String uri,
                                  @RequestParam("attribute") String attribute) {
        log.debug("检查URL权限: uri={}", uri);
        // 本地开发环境默认返回true，允许所有访问
        return true;
    }
}
