package com.liang.bbs.user.facade.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 */
@Data
public class DynamicDTO implements Serializable {
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
     * 鍙戣捣浜哄悕绉?
     */
    private String userName;

    /**
     * 鍙戣捣浜猴紙澶村儚锛?
     */
    private String picture;

    /**
     * 鎿嶄綔鐨勫璞D锛堟枃绔爄d銆佺敤鎴穒d绛夐潪璇勮id锛?
     */
    private String objectId;

    /**
     * 璇勮id
     */
    private Integer commentId;

    /**
     * 鎿嶄綔鐨勫璞″悕绉帮紙鐢ㄦ埛鍚嶇О銆佹枃绔犲悕銆佽瘎璁哄唴瀹圭瓑锛?
     */
    private String title;

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
