package com.liang.bbs.user.facade.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 */
@Data
public class FollowDTO implements Serializable {
    /**
     * 鍏虫敞缂栧彿
     */
    private Integer id;

    /**
     * 鍙戣捣鍏虫敞鐨勪汉
     */
    private Long fromUser;

    /**
     * 鐘舵€?0鍙栨秷,1鍏虫敞)
     */
    private Boolean state;

    /**
     * 琚叧娉ㄧ殑浜?
     */
    private Long toUser;

    /**
     * 鐢ㄦ埛鍚?
     */
    private String name;

    /**
     * 澶村儚
     */
    private String picture;

    /**
     * 绛夌骇锛圠v6锛?
     */
    private String level;

    /**
     * 鏄惁鍏虫敞
     */
    private Boolean isFollow;

    /**
     * 绠€浠?
     */
    private String intro;

    /**
     * 鑾峰緱鐨勭偣璧炴暟
     */
    private Long likeCount;

    /**
     * 鑾峰緱鐨勯槄璇婚噺
     */
    private Long readCount;

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
