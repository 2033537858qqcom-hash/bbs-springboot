package com.liang.bbs.article.facade.dto;

import lombok.Data;

import java.io.Serializable;

/**
 */
@Data
public class LabelSearchDTO implements Serializable {
    /**
     * 鏍囩缂栧彿
     */
    private Integer id;

    /**
     * 鏍囩鍚嶅瓧
     */
    private String labelName;

    /**
     * 閫昏緫鍒犻櫎(0姝ｅ父,1鍒犻櫎)
     */
    private Boolean isDeleted;

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
