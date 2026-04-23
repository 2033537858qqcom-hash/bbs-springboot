package com.liang.manage.auth.service.controller;

import com.liang.nansheng.common.auth.UserSsoDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/url-access-right")
public class UrlAccessRightController {

    @GetMapping("/check")
    public Boolean checkUrlAccess(UserSsoDTO currentUser,
                                  @RequestParam("uri") String uri,
                                  @RequestParam("attribute") String attribute) {
        return true;
    }
}
