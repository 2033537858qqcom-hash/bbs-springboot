package com.liang.bbs.article.facade.dto;

import lombok.Data;

import java.io.Serializable;

/**
 */
@Data
public class ArticleReadDTO implements Serializable {

    /**
     * 鐢ㄦ埛id
     */
    private Long userId;

    /**
     * 鏂囩珷闃呰閲?
     */
    private Long articleReadCount;

    private static final long serialVersionUID = 1L;

}
