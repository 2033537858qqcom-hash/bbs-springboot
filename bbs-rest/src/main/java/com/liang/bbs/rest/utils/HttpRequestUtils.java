package com.liang.bbs.rest.utils;

import com.alibaba.fastjson.JSONObject;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * HttpServletRequest鐩稿叧鐨勫伐鍏风被
 *
 */
@Slf4j
public class HttpRequestUtils {
    public static String parseCookie(HttpServletRequest request, String cookieName) {
        Cookie cookie = findCookie(request, cookieName);
        if (cookie != null) {
            return cookie.getValue();
        }
        return "default";
    }

    /**
     * 鑾峰彇cookie涓殑淇℃伅
     *
     * @param request
     * @param cookieName
     * @return
     */
    public static Cookie findCookie(HttpServletRequest request, String cookieName) {
        // 鎵€鏈夌殑 cookie
        Cookie[] cookies = request.getCookies();
        // 閬嶅巻鎵€鏈夌殑 Cookie 瀵绘壘 鐢ㄦ埛甯愬彿淇℃伅涓庣櫥褰曟鏁颁俊鎭?
        for (int i = 0; cookies != null && i < cookies.length; i++) {
            Cookie cookie = cookies[i];
            if (cookieName.equals(cookie.getName())) {
                return cookie;
            }
        }
        return null;
    }

    /**
     * 閲嶅畾鍚?
     *
     * @param request
     * @param response
     * @param location
     * @throws IOException
     */
    public static void redirect(HttpServletRequest request, HttpServletResponse response, String location) throws IOException {
        String accept = request.getHeader("accept");
        if (StringUtils.isNotEmpty(accept) && accept.contains(MediaType.APPLICATION_JSON_VALUE)) {
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            PrintWriter writer = response.getWriter();
            JSONObject o = new JSONObject();
            o.put("code", 302);
            JSONObject target = new JSONObject();
            target.put("target", location);
            o.put("data", target);
            o.put("desc", "璇峰厛鐧诲綍!");
            writer.print(o);
            writer.flush();
        } else {
            response.sendRedirect(location);
        }
    }

}
