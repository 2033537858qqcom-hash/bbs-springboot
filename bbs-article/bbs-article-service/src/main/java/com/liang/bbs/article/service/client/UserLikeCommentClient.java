package com.liang.bbs.article.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        contextId = "articleUserLikeCommentClient",
        name = "${local.services.bbs-user.name:ns-bbs-user}"
,
        path = "/internal/user/like-comment"
)
public interface UserLikeCommentClient {

    @GetMapping("/count/{commentId}")
    Long getLikeCountCommentId(@PathVariable("commentId") Integer commentId);

    @GetMapping("/is-like/{commentId}/{userId}")
    Boolean isLike(@PathVariable("commentId") Integer commentId, @PathVariable("userId") Long userId);
}
