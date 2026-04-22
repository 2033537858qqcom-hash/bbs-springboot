package com.liang.bbs.common.enums;

import lombok.Getter;

/**
 */
@Getter
public enum DynamicTypeEnum {
    /**
     * 鍐欐枃绔?
     */
    writeArticle("鍐欐枃绔?),
    likeArticle("鐐硅禐鏂囩珷"),
    likeComment("鐐硅禐璇勮"),
    commentArticle("璇勮鏂囩珷"),
    commentReply("璇勮鍥炲"),
    followUser("鍏虫敞鐢ㄦ埛");

    /**
     * 璇存槑
     */
    private String desc;

    DynamicTypeEnum(String name) {
        this.desc = name;
    }

}
