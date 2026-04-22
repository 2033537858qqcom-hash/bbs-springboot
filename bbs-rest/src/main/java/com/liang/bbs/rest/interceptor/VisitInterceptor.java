package com.liang.bbs.rest.interceptor;

import com.liang.bbs.rest.client.VisitServiceClient;
import com.liang.bbs.rest.utils.IpUtil;
import com.liang.manage.auth.facade.dto.visit.VisitDTO;
import com.liang.nansheng.common.enums.ProjectEnum;
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
public class VisitInterceptor implements HandlerInterceptor {
    @Autowired
    @Lazy
    VisitServiceClient visitService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            VisitDTO visitDTO = new VisitDTO();
            // 鐠佸潡妫堕弶銉ㄥ殰閸濐亙閲滅化鑽ょ埠
            visitDTO.setProjectId(ProjectEnum.NS_BBS.getCode());
            // ip
            visitDTO.setIp(IpUtil.getIP(request));
            // 閹垮秳缍旂化鑽ょ埠
            visitDTO.setOs(IpUtil.getOS(request));
            visitService.create(visitDTO);
        } catch (Exception e) {
            log.error("VisitInterceptor exception", e);
        }

        return true;
    }

}

