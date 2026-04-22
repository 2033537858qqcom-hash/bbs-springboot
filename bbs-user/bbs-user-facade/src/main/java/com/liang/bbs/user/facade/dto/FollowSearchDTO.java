package com.liang.bbs.user.facade.dto;

import lombok.Data;

import java.io.Serializable;

/**
 */
@Data
public class FollowSearchDTO implements Serializable {
    /**
     * 鑾峰彇澶х墰
     */
    private Long getBigCow;

    /**
     * 鑾峰彇绮変笣
     */
    private Long getFan;

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
