package com.liang.bbs.article.facade.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 */
@Data
public class ArticleDTO implements Serializable {
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
     * 鏂囩珷鍐呭锛坢arkdown锛?
     */
    private String markdown;

    /**
     * 鏂囩珷鍐呭锛坔tml锛?
     */
    private String html;

    /**
     * 鏂囩珷鎵€鎷ユ湁鐨勬爣绛?
     */
    private List<LabelDTO> labelDTOS;

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
     * 鍒涘缓鐢ㄦ埛鍚嶇О
     */
    private String createUserName;

    /**
     * 绛夌骇锛圠v6锛?
     */
    private String level;

    /**
     * 鏇存柊鐢ㄦ埛id
     */
    private Long updateUser;

    /**
     * 鏇存柊鐢ㄦ埛鍚嶇О
     */
    private String updateUserName;

    /**
     * 涓€浜涚粺璁℃暟鎹?
     */
    private ArticleCountDTO articleCountDTO;

    /**
     * 鐢ㄦ埛澶村儚
     */
    private String picture;

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
