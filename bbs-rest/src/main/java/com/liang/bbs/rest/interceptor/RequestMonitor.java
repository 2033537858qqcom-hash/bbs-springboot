package com.liang.bbs.rest.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 请求耗时监控拦截器
 *
 */
@Slf4j
@Component
public class RequestMonitor implements HandlerInterceptor {
    @Value("${interceptor.monitor.status}")
    private boolean monitorStatus;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!monitorStatus) {
            return true;
        }
        try {
            if (handler instanceof HandlerMethod) {
                log.info("class:" + ((HandlerMethod) handler).getBean().getClass().getName());
                log.info("method:" + ((HandlerMethod) handler).getMethod().getName());
            }
        } catch (Exception e) {
            log.warn("耗时监控定位失败: {}", e.getMessage());
        }
        request.setAttribute("startTime", System.currentTimeMillis());
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        if (monitorStatus) {
            Long start = (Long) request.getAttribute("startTime");
            if (start != null) {
                log.info("请求耗时: {}ms", (System.currentTimeMillis() - start));
            }
        }
    }
}
