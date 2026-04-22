package com.liang.bbs.rest.config.swagger;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ApiVersion {
    /**
     * 鎺ュ彛鐗堟湰鍙?瀵瑰簲swagger涓殑group)
     *
     * @return String[]
     */
    String[] group();
}
