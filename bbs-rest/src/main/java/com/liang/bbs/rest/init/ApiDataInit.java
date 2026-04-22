package com.liang.bbs.rest.init;

import com.liang.bbs.common.constant.RedisConstants;
import com.liang.nansheng.common.constant.PathConstants;
import com.liang.nansheng.common.enums.ProjectEnum;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 璺緞绾у埆鏉冮檺淇℃伅鏁版嵁鍒濆鍖?
 *
 */
@Slf4j
@Component
public class ApiDataInit implements ApplicationContextAware {
    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${server.servlet.context-path}")
    public String contextPath;
    /**
     * k:鏉冮檺浠ｇ爜锛寁:鏉冮檺璇存槑
     */
    public static Map<String, String> apiDescMap = new HashMap<>();

    private final List<String> removeMethods = Arrays.asList("openapiJson", "openapiYaml", "redirectToUi");

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        Map<String, Object> beanMap = ctx.getBeansWithAnnotation(Controller.class);
        initData(beanMap);
    }

    /**
     * 鍒濆鍖栨墍鏈夌殑鎺ュ彛鍜岃姹傚弬鏁?
     *
     * @param beanMap
     */
    private void initData(Map<String, Object> beanMap) {
        if (beanMap != null) {
            for (Object bean : beanMap.values()) {
                Class<?> clz = bean.getClass();
                Method[] methods = clz.getMethods();
                for (Method method : methods) {
                    // 鍒濆鍖?
                    Operation apiOperation = AnnotationUtils.findAnnotation(method, Operation.class);
                    if (apiOperation != null && !removeMethods.contains(method.getName())) {
                        // 鏉冮檺璇存槑
                        String desc = apiOperation.summary();
                        // 鏉冮檺浠ｇ爜
                        String uri = getApiUri(clz, method);
                        apiDescMap.put(uri, desc);
                    }
                }
            }
        }
        String key = RedisConstants.API_KEY + ProjectEnum.NS_BBS.getCode();
        redisTemplate.opsForValue().set(key, apiDescMap);
        log.info("銆恆piDescMap銆?{}", apiDescMap);
    }

    /**
     * 鏋勯€犺姹傜被鍨嬪拰璇锋眰鍦板潃骞惰繑鍥?
     *
     * @param clz
     * @param method
     * @return
     */
    private String getApiUri(Class<?> clz, Method method) {
        String methodType = "";
        StringBuilder uri = new StringBuilder();

        // Controller绫绘湁@RequestMapping鐨勬儏鍐?
        RequestMapping reqMapping = AnnotationUtils.findAnnotation(clz, RequestMapping.class);
        if (reqMapping != null) {
            uri.append(formatUri(reqMapping.value()[0]));
        }

        // 鏂规硶涓婃湁@RequestMapping鐨勬儏鍐?
        GetMapping getMapping = AnnotationUtils.findAnnotation(method, GetMapping.class);
        if (getMapping != null) {
            methodType = RequestMethod.GET.name();
            if (uri.toString().endsWith(PathConstants.PATH_SEPARATOR)) {
                uri.delete(uri.length() - 1, uri.length());
            }
            uri.append(formatUri(getMapping.value()[0]));
        }
        PostMapping postMapping = AnnotationUtils.findAnnotation(method, PostMapping.class);
        if (postMapping != null) {
            methodType = RequestMethod.POST.name();
            if (uri.toString().endsWith(PathConstants.PATH_SEPARATOR)) {
                uri.delete(uri.length() - 1, uri.length());
            }
            uri.append(formatUri(postMapping.value()[0]));
        }
        PutMapping putMapping = AnnotationUtils.findAnnotation(method, PutMapping.class);
        if (putMapping != null) {
            methodType = RequestMethod.PUT.name();
            if (uri.toString().endsWith(PathConstants.PATH_SEPARATOR)) {
                uri.delete(uri.length() - 1, uri.length());
            }
            uri.append(formatUri(putMapping.value()[0]));
        }
        DeleteMapping deleteMapping = AnnotationUtils.findAnnotation(method, DeleteMapping.class);
        if (deleteMapping != null) {
            methodType = RequestMethod.DELETE.name();
            if (uri.toString().endsWith(PathConstants.PATH_SEPARATOR)) {
                uri.delete(uri.length() - 1, uri.length());
            }
            uri.append(formatUri(deleteMapping.value()[0]));
        }
        RequestMapping requestMapping = AnnotationUtils.findAnnotation(method, RequestMapping.class);
        if (requestMapping != null && !StringUtils.hasText(methodType)) {
            if (uri.toString().endsWith(PathConstants.PATH_SEPARATOR)) {
                uri.delete(uri.length() - 1, uri.length());
            }
            uri.append(formatUri(requestMapping.value()[0]));
        }

        return contextPath + uri;
    }

    /**
     * 鏋勯€爑ri
     *
     * @param uri
     * @return
     */
    private String formatUri(String uri) {
        if (uri.startsWith(PathConstants.PATH_SEPARATOR)) {
            return uri;
        }
        return PathConstants.PATH_SEPARATOR + uri;
    }

}
