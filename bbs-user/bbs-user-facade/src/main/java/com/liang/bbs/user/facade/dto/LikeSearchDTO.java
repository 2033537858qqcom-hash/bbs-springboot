package com.liang.bbs.user.facade.dto;

import lombok.Data;

import java.io.Serializable;

/**
 */
@Data
public class LikeSearchDTO implements Serializable {

    /**
     * 鏂囩珷id
     */
    private Integer articleId;

    /**
     * 鐐硅禐鐢ㄦ埛id
     */
    private Long likeUser;

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
