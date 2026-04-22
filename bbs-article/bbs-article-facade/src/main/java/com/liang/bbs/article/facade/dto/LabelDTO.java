package com.liang.bbs.article.facade.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 */
@Data
public class LabelDTO implements Serializable {
    /**
     * 鏍囩缂栧彿
     */
    private Integer id;

    /**
     * 鏍囩鍚嶅瓧
     */
    private String labelName;

    /**
     * logo(鍥剧墖)
     */
    private String logo;

    /**
     * 鏂囩珷浣跨敤鏁伴噺
     */
    private Long articleUseCount;

    /**
     * 閫昏緫鍒犻櫎(0姝ｅ父,1鍒犻櫎)
     */
    private Boolean isDeleted;

    /**
     * 鍒涘缓鐢ㄦ埛id
     */
    private Long createUser;

    /**
     * 鏇存柊鐢ㄦ埛id
     */
    private Long updateUser;

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
