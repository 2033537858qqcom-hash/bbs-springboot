package com.liang.bbs.common.enums;

import lombok.Getter;

/**
 * 动态类型枚举
 */
@Getter
public enum DynamicTypeEnum {
    /**
     * 写文章
     */
    writeArticle("写文章"),
    likeArticle("点赞文章"),
    likeComment("点赞评论"),
    commentArticle("评论文章"),
    commentReply("评论回复"),
    followUser("关注用户");

    /**
     * 说明
     */
    private String desc;

    DynamicTypeEnum(String name) {
        this.desc = name;
    }

}
