package com.liang.bbs.article.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 瀵瑰簲鏁版嵁琛ㄤ负锛歠s_slideshow
 * 
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SlideshowPo implements Serializable {
    /**
     * 杞挱鍥剧紪鍙?
     */
    private Integer id;

    /**
     * 鍚嶇О
     */
    private String name;

    /**
     * 杞挱鍥?
     */
    private String image;

    /**
     * 璺宠浆鍦板潃
     */
    private String jumpAddress;

    /**
     * 鎻忚堪
     */
    private String desc;

    /**
     * 鐘舵€?0绂佺敤,1鍚敤)
     */
    private Boolean state;

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
