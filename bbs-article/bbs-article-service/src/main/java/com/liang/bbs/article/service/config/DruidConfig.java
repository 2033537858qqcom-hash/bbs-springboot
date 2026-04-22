package com.liang.bbs.article.service.config;

import com.alibaba.druid.support.jakarta.StatViewServlet;
import com.alibaba.druid.support.jakarta.WebStatFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 褰撴暟鎹簱杩炴帴姹犱娇鐢╠ruid鏃讹紝鎴戜滑杩涜涓€浜涚畝鍗曠殑閰嶇疆灏辫兘鏌ョ湅鍒皊ql鐩戞帶锛寃eb鐩戞帶锛寀rl鐩戞帶绛夌瓑
 *
 */
@Configuration
public class DruidConfig {

    /**
     * 娉ㄥ唽ServletRegistrationBean
     *
     * @return
     */
    @Bean
    public ServletRegistrationBean<StatViewServlet> registrationBean() {
        ServletRegistrationBean<StatViewServlet> bean = new ServletRegistrationBean<>(new StatViewServlet(), "/druid/*");
        /** 鍒濆鍖栧弬鏁伴厤缃紝initParams**/
        //鐧藉悕鍗?
        bean.addInitParameter("allow", "127.0.0.1,47.119.192.69,120.48.100.28");
        //IP榛戝悕鍗?(瀛樺湪鍏卞悓鏃讹紝deny浼樺厛浜巃llow) : 濡傛灉婊¤冻deny鐨勮瘽鎻愮ず:Sorry, you are not permitted to view this page.
        bean.addInitParameter("deny", "192.168.1.73");
        //鐧诲綍鏌ョ湅淇℃伅鐨勮处鍙峰瘑鐮?
        bean.addInitParameter("loginUsername", "admin");
        bean.addInitParameter("loginPassword", "admin");
        //鏄惁鑳藉閲嶇疆鏁版嵁.
        bean.addInitParameter("resetEnable", "false");
        return bean;
    }

    /**
     * 娉ㄥ唽FilterRegistrationBean
     *
     * @return
     */
    @Bean
    public FilterRegistrationBean<WebStatFilter> druidStatFilter() {
        FilterRegistrationBean<WebStatFilter> bean = new FilterRegistrationBean<>(new WebStatFilter());
        //娣诲姞杩囨护瑙勫垯.
        bean.addUrlPatterns("/*");
        //娣诲姞涓嶉渶瑕佸拷鐣ョ殑鏍煎紡淇℃伅.
        bean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        return bean;
    }

}
