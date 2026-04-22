package com.liang.bbs.common.enums;

import lombok.Getter;

/**
 */
@Getter
public enum ArticleStateEnum {
    /**
     * 寰呭鏍?
     */
    pendingReview(-1, "寰呭鏍?),
    disabled(0, "绂佺敤"),
    enable(1, "鍚敤");

    /**
     * 绉垎
     */
    private Integer code;

    /**
     * 璇存槑
     */
    private String desc;

    ArticleStateEnum(Integer code, String name) {
        this.code = code;
        this.desc = name;
    }

}
