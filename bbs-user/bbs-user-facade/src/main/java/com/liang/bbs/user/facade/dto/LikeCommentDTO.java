package com.liang.bbs.user.facade.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 */
@Data
public class LikeCommentDTO implements Serializable {
    /**
     * 璇勮鐐硅禐缂栧彿
     */
    private Integer id;

    /**
     * 璇勮id
     */
    private Integer commentId;

    /**
     * 鐘舵€?0鍙栨秷,1鐐硅禐)
     */
    private Boolean state;

    /**
     * 璇勮鐐硅禐鐢ㄦ埛id
     */
    private Long likeUser;

    /**
     * 鍒涘缓鏃堕棿
     */
    private LocalDateTime createTime;

    /**
     * 鏇存柊鏃堕棿
     */
    private LocalDateTime updateTime;

    private static final long serialVersionUID = 1L;

}
