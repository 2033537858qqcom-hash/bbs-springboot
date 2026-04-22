package com.liang.bbs.article.facade.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 */
@Data
public class ArticleSearchDTO implements Serializable {
    /**
     * 鏂囩珷缂栧彿
     */
    private Integer id;

    /**
     * 鏂囩珷鏍囬
     */
    private String title;

    /**
     * 鏍囩缂栧彿
     */
    private List<Integer> labelIds;

    /**
     * 鍒涘缓鐢ㄦ埛id
     */
    private Long createUser;

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
