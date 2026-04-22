package com.liang.bbs.rest.interceptor;

import com.alibaba.fastjson.JSON;
import com.liang.bbs.rest.client.UrlAccessRightServiceClient;
import com.liang.nansheng.common.auth.UserContextUtils;
import com.liang.nansheng.common.auth.UserSsoDTO;
import com.liang.nansheng.common.enums.ResponseCode;
import com.liang.nansheng.common.web.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 閸氬海顏捄顖氱窞缁狙冨焼閻ㄥ嫭娼堥梽鎰付閸?
 *
 */
@Slf4j
@Component
public class UrlAccessCheckInterceptor implements HandlerInterceptor {
    @Autowired
    @Lazy
    UrlAccessRightServiceClient urlAccessRightService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        UserSsoDTO currentUser = UserContextUtils.currentUser();
        if (currentUser != null) {
            String uri = request.getRequestURI();
            // 瀵版鍩?@PathVariable 閻ㄥ嫬寮弫鏉挎嫲閸?
            Object attribute = request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
            Boolean b = urlAccessRightService.checkUrlAccess(currentUser, uri, JSON.toJSONString(attribute));
            if (!b) {
                log.info("鐠佸潡妫堕弮鐘虫綀闂勬劗娈戦幒銉ュ經閿涘瘈ri={}, user={}", uri, currentUser);
                throw BusinessException.build(ResponseCode.URL_ACCESS_REFUSED);
            }
        }
        return true;
    }

}

