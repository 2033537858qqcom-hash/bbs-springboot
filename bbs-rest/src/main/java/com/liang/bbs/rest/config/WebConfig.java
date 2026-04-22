package com.liang.bbs.rest.config;

import com.liang.bbs.rest.interceptor.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * 閰嶇疆绫?
 *
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    /**
     * 涓嶉渶瑕佺櫥褰曠殑鎺ュ彛
     */
    private static final String[] NOT_LOGIN_URLS = {
            "/swagger-resources/**",
            "/webjars/**",
            "/v2/**",
            "/v3/**",
            "/doc.html/**",
            "/swagger-ui.html/**",
            "/springdoc/**",
            "/favicon.ico/**",
            "/error/**"
    };

    @Autowired
    private RequestMonitor requestMonitor;

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Autowired
    private UrlAccessCheckInterceptor urlAccessCheckInterceptor;

    @Autowired
    private UserPictureInterceptor userPictureInterceptor;

    @Autowired
    private VisitInterceptor visitInterceptor;

    @Autowired
    private NotifyInterceptor notifyInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 鐩戞帶璇锋眰鑰楁椂绛変俊鎭嫤鎴櫒
        registry.addInterceptor(requestMonitor)
                .addPathPatterns("/**")
                .excludePathPatterns(NOT_LOGIN_URLS);

        // 鐢ㄦ埛鏈夋晥鎬ч獙璇佹嫤鎴櫒
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(NOT_LOGIN_URLS);

        // 鍚庣璺緞绾у埆鐨勮闂潈闄愭帶鍒?
        registry.addInterceptor(urlAccessCheckInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(NOT_LOGIN_URLS);

        // 鐢ㄦ埛澶村儚鎷︽埅鍣?
        registry.addInterceptor(userPictureInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(NOT_LOGIN_URLS);

        // 璁块棶璁板綍鎷︽埅鍣?
        registry.addInterceptor(visitInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(NOT_LOGIN_URLS);

        // 娑堟伅閫氱煡鎷︽埅鍣?
        registry.addInterceptor(notifyInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(NOT_LOGIN_URLS);

    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // doc.html鏂囦欢锛岄兘浼氬幓鍚庨潰閰嶇疆鐨勮矾寰勪笅鏌ユ壘璧勬簮
        registry.addResourceHandler("doc.html")
                // 瑕佸紑鏀剧殑璧勬簮
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

}
