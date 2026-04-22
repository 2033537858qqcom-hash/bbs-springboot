package com.liang.bbs.article.facade.dto;

import lombok.Data;

import java.io.Serializable;

/**
 */
@Data
public class ArticleCountDTO implements Serializable {
    /**
     * 鏄惁鐐硅禐
     */
    private Boolean isLike;

    /**
     * 鐐硅禐鏁伴噺
     */
    private Long likeCount;

    /**
     * 璇勮鏁伴噺
     */
    private Long commentCount;

    /**
     * 鏄惁鍏虫敞
     */
    private Boolean isFollow;

    /**
     * 绛夌骇锛圠v6锛?
     */
    private String level;

    private static final long serialVersionUID = 1L;

}
