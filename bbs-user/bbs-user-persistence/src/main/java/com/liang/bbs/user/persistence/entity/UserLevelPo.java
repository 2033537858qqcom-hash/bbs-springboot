package com.liang.bbs.user.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 瀵瑰簲鏁版嵁琛ㄤ负锛歠s_user_level
 * 
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserLevelPo implements Serializable {
    /**
     * 鐢ㄦ埛绛夌骇缂栧彿
     */
    private Integer id;

    /**
     * 鐢ㄦ埛id
     */
    private Long userId;

    /**
     * 绛夌骇锛圠v6锛?
     */
    private String level;

    /**
     * 绉垎
     */
    private Integer points;

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
