package com.liang.bbs.rest.interceptor;

import com.liang.bbs.rest.client.UserServiceClient;
import com.liang.manage.auth.facade.dto.user.UserDTO;
import com.liang.nansheng.common.auth.UserContextUtils;
import com.liang.nansheng.common.auth.UserSsoDTO;
import com.liang.nansheng.common.constant.HeaderConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 閻劍鍩涙径鏉戝剼閹凤附鍩呴崳?
 *
 */
@Slf4j
@Component
public class UserPictureInterceptor implements HandlerInterceptor {
    @Autowired
    @Lazy
    UserServiceClient userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        UserSsoDTO currentUser = UserContextUtils.currentUser();
        if (currentUser != null) {
            try {
                UserDTO userDTO = userService.getById(currentUser.getUserId());
                if (userDTO != null && userDTO.getPicture() != null) {
                    response.addHeader(HeaderConstants.USER_PICTURE_HEADER, userDTO.getPicture());
                }
            } catch (Exception e) {
                log.warn("manage-auth unavailable, skip user picture header", e);
            }
        }
        return true;
    }

}

