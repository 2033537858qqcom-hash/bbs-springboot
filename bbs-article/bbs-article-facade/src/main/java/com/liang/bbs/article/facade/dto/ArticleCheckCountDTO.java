package com.liang.bbs.article.facade.dto;

import lombok.Data;

import java.io.Serializable;

/**
 */
@Data
public class ArticleCheckCountDTO implements Serializable {

    /**
     * 鍚敤鏂囩珷鏁伴噺
     */
    private Long enableCount;

    /**
     * 绂佺敤鏂囩珷鏁伴噺
     */
    private Long disabledCount;

    /**
     * 寰呭鏍告枃绔犳暟閲?
     */
    private Long pendingReviewCount;


    private static final long serialVersionUID = 1L;

}
