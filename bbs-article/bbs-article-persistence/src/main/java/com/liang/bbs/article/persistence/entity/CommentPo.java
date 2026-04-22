package com.liang.bbs.article.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 瀵瑰簲鏁版嵁琛ㄤ负锛歠s_comment
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentPo implements Serializable {
    /**
     * 璇勮缂栧彿
     */
    private Integer id;

    /**
     * 鐖惰瘎璁篿d
     */
    private Integer preId;

    /**
     * 璇勮鍐呭
     */
    private String content;

    /**
     * 琚瘎璁哄笘瀛恑d
     */
    private Integer articleId;

    /**
     * 鐘舵€?0绂佺敤,1鍚敤)
     */
    private Boolean state;

    /**
     * 閫昏緫鍒犻櫎(0姝ｅ父,1鍒犻櫎)
     */
    private Boolean isDeleted;

    /**
     * 璇勮鐢ㄦ埛id
     */
    private Long commentUser;

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
