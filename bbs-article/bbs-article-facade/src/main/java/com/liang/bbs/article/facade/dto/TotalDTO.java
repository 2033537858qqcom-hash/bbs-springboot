package com.liang.bbs.article.facade.dto;

import lombok.Data;

import java.io.Serializable;

/**
 */
@Data
public class TotalDTO implements Serializable {

    /**
     * 鏂囩珷鏁伴噺
     */
    private Long articleCount;

    /**
     * 璇勮鏁伴噺
     */
    private Long commentCount;

    /**
     * 璁块棶鏁伴噺
     */
    private Long visitCount;

    private static final long serialVersionUID = 1L;

}
