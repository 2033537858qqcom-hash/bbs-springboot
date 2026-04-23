package com.liang.bbs.rest.interceptor;

import com.liang.bbs.rest.client.NotifyServiceClient;
import com.liang.nansheng.common.auth.UserContextUtils;
import com.liang.nansheng.common.auth.UserSsoDTO;
import com.liang.nansheng.common.constant.HeaderConstants;
import com.liang.nansheng.common.enums.NotifyTypeEnum;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 閻劍鍩涙径鏉戝剼閹凤附鍩呴崳?
 *
 */
@Slf4j
@Component
public class NotifyInterceptor implements HandlerInterceptor {
    @Autowired
    @Lazy
    NotifyServiceClient notifyService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 放行 OPTIONS 请求
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        UserSsoDTO currentUser = UserContextUtils.currentUser();
        if (currentUser != null) {
            response.addHeader(HeaderConstants.SYSTEM_NOTIFY_HEADER,
                    String.valueOf(notifyService.getNotReadNotifyCount(currentUser.getUserId(), NotifyTypeEnum.NS_SYSTEM_NOTIFY.getCode())));
            response.addHeader(HeaderConstants.TASK_NOTIFY_HEADER,
                    String.valueOf(notifyService.getNotReadNotifyCount(currentUser.getUserId(), NotifyTypeEnum.NS_TASK_REMINDER.getCode())));
        }

        return true;
    }

}

