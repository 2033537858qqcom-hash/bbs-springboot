package com.liang.bbs.rest.utils;

import lombok.extern.slf4j.Slf4j;

import jakarta.servlet.http.HttpServletRequest;
import java.util.StringTokenizer;

/**
 * ip瑙ｆ瀽
 *
 */
@Slf4j
public class IpUtil {

    /**
     * 鑾峰彇璁块棶鑰卛p
     *
     * @param request
     * @return
     */
    public static String getIP(HttpServletRequest request) {
        /**
         * X-Forwarded-For:绠€绉癤FF澶达紝瀹冧唬琛ㄥ鎴风锛屼篃灏辨槸HTTP鐨勮姹傜鐪熷疄鐨処P 鍙湁鍦ㄩ€氳繃浜咹TTP 浠ｇ悊鎴栬€呰礋杞藉潎琛℃湇鍔″櫒鏃舵墠浼氭坊鍔犺椤?
         * 鏍囧噯鏍煎紡濡備笅锛歑-Forwarded-For: client_ip, proxy1_ip, proxy2_ip
         * 姝ゅご鏄彲鏋勯€犵殑锛屽洜姝ゆ煇浜涘簲鐢ㄤ腑搴旇瀵硅幏鍙栧埌鐨刬p杩涜楠岃瘉
         */
        String ip = request.getHeader("X-Real-IP");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        /**
         * 鍦ㄥ绾т唬鐞嗙綉缁滀腑锛岀洿鎺ョ敤getHeader("x-forwarded-for")鍙兘鑾峰彇鍒扮殑鏄痷nknown淇℃伅
         * 姝ゆ椂闇€瑕佽幏鍙栦唬鐞嗕唬鐞嗘湇鍔″櫒閲嶆柊鍖呰鐨凥TTP澶翠俊鎭紝
         */
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknow".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }

        return ip;
    }

    /**
     * 鑾峰彇璁块棶鑰呮搷浣滅郴缁?
     *
     * @param request
     * @return
     */
    public static String getOS(HttpServletRequest request) {
        String agent = request.getHeader("User-Agent");
        log.info("User-Agent: {}", agent);
        StringTokenizer st = new StringTokenizer(agent, ";");
        st.nextToken();
        // 寰楀埌璁块棶鑰呯殑鎿嶄綔绯荤粺鍚?
        String os = st.nextToken();

        // 浼樺寲鎿嶄綔绯荤粺鍚?win
        boolean isWin2K = agent.contains("Windows NT 5.0") || agent.contains("Windows 2000");
        if (isWin2K) {
            os = "Windows 2000";
        }
        boolean isWinXP = agent.contains("Windows NT 5.1") || agent.contains("Windows XP");
        if (isWinXP) {
            os = "Windows XP";
        }
        boolean isWin2003 = agent.contains("Windows NT 5.2") || agent.contains("Windows 2003");
        if (isWin2003) {
            os = "Windows 2003";
        }
        boolean isWinVista = agent.contains("Windows NT 6.0") || agent.contains("Windows Vista");
        if (isWinVista) {
            os = "Windows Vista";
        }
        boolean isWin7 = agent.contains("Windows NT 6.1") || agent.contains("Windows 7");
        if (isWin7) {
            os = "Windows 7";
        }
        boolean isWin8 = agent.contains("Windows NT 6.2") || agent.contains("Windows NT 6.3") || agent.contains("Windows 8");
        if (isWin8) {
            os = "Windows 8";
        }
        boolean isWin10 = agent.contains("Windows NT 10") || agent.contains("Windows 10");
        if (isWin10) {
            os = "Windows 10";
        }
        // mac
        boolean mac = agent.contains("Mac OS X");
        if (mac) {
            os = "Mac OS X";
        }
        // linux
        boolean linux = agent.contains("Linux x86_64");
        if (linux) {
            os = "Linux x86_64";
        }
        // Android 5.0) AppleWebKit
        boolean android = agent.contains("Android 5.0) AppleWebKit");
        if (android) {
            os = "Android 5.0";
        }

        // 鐗规畩澶勭悊U鐨勬儏鍐?
        if ("U".equalsIgnoreCase(os.trim())) {
            os = st.nextToken();
        }

        return os;
    }

}
