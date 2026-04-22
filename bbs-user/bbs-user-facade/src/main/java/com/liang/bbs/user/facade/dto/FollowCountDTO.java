package com.liang.bbs.user.facade.dto;

import lombok.Data;

import java.io.Serializable;

/**
 */
@Data
public class FollowCountDTO implements Serializable {

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
