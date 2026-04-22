package com.liang.bbs.rest.interceptor;

import com.liang.bbs.rest.client.UserServiceClient;
import com.liang.bbs.rest.config.login.NoNeedLogin;
import com.liang.bbs.rest.utils.HttpRequestUtils;
import com.liang.nansheng.common.auth.UserContextUtils;
import com.liang.nansheng.common.auth.UserSsoDTO;
import com.liang.nansheng.common.constant.AuthSystemConstants;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.Objects;


/**
 * 妤犲矁鐦夐悽銊﹀煕閻ㄥ嫭婀侀弫鍫熲偓褝绱濋弮鐘虫櫏閸掓瑩鍣哥€规艾鎮滈崚鎵瑜版洟銆夐棃銏ｇ箻鐞涘瞼娅ヨぐ?
 *
 */
@Slf4j
@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    @Lazy
    private UserServiceClient userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        UserSsoDTO currentUser = null;
        // 閼惧嘲褰嘽ookie娑擃厾娈戞穱鈩冧紖
        Cookie ssoAccount = HttpRequestUtils.findCookie(request, AuthSystemConstants.NS_ACCOUNT_SSO_COOKIE);

        // 娴ｈ法鏁ら悽銊﹀煕閸氬秴鐦戦惍浣风矤sso閻ц缍?
        if (ssoAccount != null) {
            try {
                currentUser = sso(request, ssoAccount);
            } catch (Exception e) {
                log.warn("manage-auth unavailable, skip sso check", e);
            }
        }
        // 閻劍鍩涙稉濠佺瑓閺傚洩顔曠純顔藉潑閸旂姷鏁ら幋铚備繆閹垽绱欑痪璺ㄢ柤閸欐﹢鍣洪敍?
        UserContextUtils.setCurrentUser(currentUser);

        // 閺冪娀娓堕惂璇茬秿
        if (handler instanceof HandlerMethod) {
            // 閺傝纭舵稉濠勬畱濞夈劏袙
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            NoNeedLogin methodAnnotation = handlerMethod.getMethodAnnotation(NoNeedLogin.class);
            if (Objects.nonNull(methodAnnotation)) {
                return true;
            }
            // 缁绗傞惃鍕暈鐟?
            Class<?> clazz = handlerMethod.getBeanType();
            if (Objects.nonNull(AnnotationUtils.findAnnotation(clazz, NoNeedLogin.class))) {
                return true;
            }
        }

        // 閼惧嘲褰囬悽銊﹀煕娣団剝浼呮径杈Е閿涘矁鐑︽潪顒傛瑜版洟銆夐棃?
        if (currentUser == null) {
            // 閼惧嘲褰囪ぐ鎾冲妞ょ敻娼伴崷鏉挎絻
            String referer = request.getHeader("referer");
            if (StringUtils.isBlank(referer)) {
                referer = request.getRequestURL().toString();
            }
            if (referer.contains("?")) {
                referer = referer.substring(0, referer.indexOf("?"));
            }
            try {
                String redirect = userService.innerLoginUrl(referer);
                log.info("閻ц缍嶇捄瀹犳祮[{}]", redirect);
                HttpRequestUtils.redirect(request, response, redirect);
            } catch (Exception e) {
                log.warn("manage-auth unavailable, cannot build login redirect for referer={}", referer, e);
                response.sendError(HttpServletResponse.SC_SERVICE_UNAVAILABLE, "本地认证服务未启动: ns-manage-auth");
            }
            return false;
        }
        return true;
    }

    /**
     * sso闁村瓨娼堟径鍕倞閿涘矁骞忛崣鏍瑜版洜鏁ら幋铚備繆閹?
     *
     * @param request
     * @return
     * @throws IOException
     */
    private UserSsoDTO sso(HttpServletRequest request, Cookie cookie) throws Exception {
        String token = cookie.getValue();
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        // 閺嶏繝鐛橳oken閺勵垰鎯佸锝団€?
        if (!userService.verifyToken(token)) {
            return null;
        }
        // token瀹歌尪绻冮張?
        if (userService.isExpired(token)) {
            return null;
        }

        // 闁俺绻僼oken閼惧嘲褰囬悽銊﹀煕娣団剝浼?
        return userService.getUserSsoDTOByToken(token);
    }

}


