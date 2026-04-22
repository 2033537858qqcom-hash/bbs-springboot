package com.liang.bbs.article.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 瀵瑰簲鏁版嵁琛ㄤ负锛歠s_article_label
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleLabelPo implements Serializable {
    /**
     * 鏂囩珷鏍囩缂栧彿
     */
    private Integer id;

    /**
     * 鏂囩珷id
     */
    private Integer articleId;

    /**
     * 鏍囩id
     */
    private Integer labelId;

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
