package com.liang.bbs.user.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 瀵瑰簲鏁版嵁琛ㄤ负锛歠s_follow
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FollowPo implements Serializable {
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
     * 鍒涘缓鏃堕棿
     */
    private LocalDateTime createTime;

    /**
     * 鏇存柊鏃堕棿
     */
    private LocalDateTime updateTime;

    private static final long serialVersionUID = 1L;
}
