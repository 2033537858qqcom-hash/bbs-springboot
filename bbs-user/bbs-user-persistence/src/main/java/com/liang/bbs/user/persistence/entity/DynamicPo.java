package com.liang.bbs.user.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 瀵瑰簲鏁版嵁琛ㄤ负锛歠s_dynamic
 * 
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DynamicPo implements Serializable {
    /**
     * 鐢ㄦ埛鍔ㄦ€佺紪鍙?
     */
    private Integer id;

    /**
     * 绫诲瀷锛堝啓鏂囩珷銆佽瘎璁恒€佺偣璧炪€佸叧娉ㄧ瓑锛?
     */
    private String type;

    /**
     * 鍙戣捣浜?
     */
    private Long userId;

    /**
     * 鎿嶄綔鐨勫璞D锛堟枃绔爄d銆佺敤鎴穒d绛夛級
     */
    private String objectId;

    /**
     * 璇勮id
     */
    private Integer commentId;

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
