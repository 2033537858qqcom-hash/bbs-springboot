package com.liang.bbs.rest.client;

import com.github.pagehelper.PageInfo;
import com.liang.bbs.article.facade.dto.CommentDTO;
import com.liang.bbs.article.facade.dto.InternalCommentOperateRequest;
import com.liang.bbs.article.facade.dto.InternalCommentQueryRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(
        contextId = "articleCommentClient",
        name = "${local.services.bbs-article.name:ns-bbs-article}"
,
        path = "/internal/article/comment"
)
public interface ArticleCommentClient {

    @PostMapping("/article-list")
    List<CommentDTO> getCommentByArticleId(@RequestBody InternalCommentQueryRequest request);

    @PostMapping("/latest")
    PageInfo<CommentDTO> getLatestComment(@RequestBody InternalCommentQueryRequest request);

    @PostMapping("/create")
    Boolean create(@RequestBody InternalCommentOperateRequest request);

    @PostMapping("/delete/{commentId}")
    Boolean delete(@PathVariable("commentId") Integer commentId);
}
