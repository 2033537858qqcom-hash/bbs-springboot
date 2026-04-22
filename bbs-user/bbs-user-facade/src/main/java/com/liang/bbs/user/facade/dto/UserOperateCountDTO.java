package com.liang.bbs.user.facade.dto;

import lombok.Data;

import java.io.Serializable;

/**
 */
@Data
public class UserOperateCountDTO implements Serializable {

    /**
     * 鏂囩珷鏁伴噺
     */
    private Long articleCount;

    /**
     * 鐐硅禐鏁伴噺
     */
    private Long likeCount;

    /**
     * 鍏虫敞鏁伴噺
     */
    private Long followCount;

    /**
     * 绮変笣鏁伴噺
     */
    private Long fanCount;

    private static final long serialVersionUID = 1L;

}
