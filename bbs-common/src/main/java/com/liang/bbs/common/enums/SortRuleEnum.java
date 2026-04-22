package com.liang.bbs.common.enums;

import lombok.Getter;

/**
 */
@Getter
public enum SortRuleEnum {
    /**
     * 鏈€鐑?
     */
    hottest("鏈€鐑?),
    newest("鏈€鏂?);

    /**
     * 璇存槑
     */
    private String desc;

    SortRuleEnum(String name) {
        this.desc = name;
    }

}
