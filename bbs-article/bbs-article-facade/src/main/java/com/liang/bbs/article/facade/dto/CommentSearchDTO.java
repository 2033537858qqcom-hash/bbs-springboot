package com.liang.bbs.article.facade.dto;

import com.liang.bbs.common.enums.SortRuleEnum;
import lombok.Data;

import java.io.Serializable;

/**
 */
@Data
public class CommentSearchDTO implements Serializable {
    /**
     * 璇勮缂栧彿
     */
    private Integer id;

    /**
     * 璇勮鍐呭
     */
    private String content;

    /**
     * 璇勮鐢ㄦ埛id
     */
    private Long commentUser;

    /**
     * 鏂囩珷id
     */
    private Integer articleId;

    /**
     * 鎺掑簭瑙勫垯
     */
    private SortRuleEnum sortRule;

    /**
     * 褰撳墠椤?
     */
    private Integer currentPage;

    /**
     * 姣忛〉鏉℃暟
     */
    private Integer pageSize;

    private static final long serialVersionUID = 1L;

}
