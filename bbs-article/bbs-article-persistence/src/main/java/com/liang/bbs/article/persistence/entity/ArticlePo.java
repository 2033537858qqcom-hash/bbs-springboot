package com.liang.bbs.article.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 瀵瑰簲鏁版嵁琛ㄤ负锛歠s_article
 * 
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticlePo implements Serializable {
    /**
     * 鏂囩珷缂栧彿
     */
    private Integer id;

    /**
     * 棰樺浘
     */
    private String titleMap;

    /**
     * 鏂囩珷鏍囬
     */
    private String title;

    /**
     * 鏂囩珷鍐呭
     */
    private String content;

    /**
     * 鐘舵€?-1寰呭鏍?0绂佺敤,1鍚敤)
     */
    private Integer state;

    /**
     * 鏂囩珷娴忚閲?
     */
    private Integer pv;

    /**
     * 缃《锛堟暟瀛楄秺澶ц秺缃《锛?
     */
    private Integer top;

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
