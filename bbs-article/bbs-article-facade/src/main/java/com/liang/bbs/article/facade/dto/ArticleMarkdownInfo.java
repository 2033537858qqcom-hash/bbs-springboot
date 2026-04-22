package com.liang.bbs.article.facade.dto;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 */
@Data
@Document("bbs_article_markdown_info")
public class ArticleMarkdownInfo {
    @Id
    private String id;

    /**
     * 鏂囩珷id
     */
    private Integer articleId;

    /**
     * 鏂囩珷鍐呭markdown
     */
    private String articleMarkdown;

    /**
     * 鏂囩珷鍐呭html
     */
    private String articleHtml;

    /**
     * 鐢ㄦ埛id
     */
    private Long userId;

    /**
     * 鏃堕棿锛堝垱寤?鏇存柊锛?
     */
    private LocalDateTime time;
}
