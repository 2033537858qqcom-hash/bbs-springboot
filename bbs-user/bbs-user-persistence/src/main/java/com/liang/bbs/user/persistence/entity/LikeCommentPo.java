package com.liang.bbs.user.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 瀵瑰簲鏁版嵁琛ㄤ负锛歠s_comment_like
 * 
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LikeCommentPo implements Serializable {
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
