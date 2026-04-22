package com.liang.bbs.article.facade.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 */
@Data
public class CommentDTO implements Serializable {
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
     * 璇勮鐢ㄦ埛鍚嶇О
     */
    private String commentUserName;

    /**
     * 鐢ㄦ埛澶村儚
     */
    private String picture;

    /**
     * 绛夌骇锛圠v6锛?
     */
    private String level;

    /**
     * 鏄惁鐐硅禐
     */
    private Boolean isLike;

    /**
     * 鐐硅禐鏁伴噺
     */
    private Long likeCount;

    /**
     * 鍥炲鏁伴噺
     */
    private Integer repliesCount;

    /**
     * 璇勮娣卞害
     */
    private Integer depth;

    /**
     * 鍒涘缓鏃堕棿
     */
    private LocalDateTime createTime;

    /**
     * 鏇存柊鏃堕棿
     */
    private LocalDateTime updateTime;

    private List<CommentDTO> child;

    private static final long serialVersionUID = 1L;

}
