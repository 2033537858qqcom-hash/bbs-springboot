package com.liang.bbs.user.service.client;

import com.liang.bbs.article.facade.dto.CommentDTO;
import com.liang.bbs.article.facade.dto.InternalTimeRangeRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(
        contextId = "userArticleCommentClient",
        name = "${local.services.bbs-article.name:ns-bbs-article}"
,
        path = "/internal/article/comment"
)
public interface ArticleCommentClient {

    @PostMapping("/all-article-comments")
    List<CommentDTO> getAllArticleComment(@RequestBody InternalTimeRangeRequest request);

    @PostMapping("/all-comment-reply")
    List<CommentDTO> getAllCommentReply(@RequestBody InternalTimeRangeRequest request);

    @GetMapping("/article-id/{commentId}")
    Integer getArticleIdByCommentId(@PathVariable("commentId") Integer commentId);

    @GetMapping("/by-id/{commentId}")
    CommentDTO getById(@PathVariable("commentId") Integer commentId);
}
