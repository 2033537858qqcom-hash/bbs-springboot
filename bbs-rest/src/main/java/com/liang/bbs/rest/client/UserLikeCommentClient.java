package com.liang.bbs.rest.client;

import com.liang.bbs.user.facade.dto.InternalLikeCommentOperateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        contextId = "userLikeCommentClient",
        name = "${local.services.bbs-user.name:ns-bbs-user}", url = "${local.services.bbs-user.url}"
,
        path = "/internal/user/like-comment"
)
public interface UserLikeCommentClient {

    @PostMapping("/state")
    Boolean updateLikeCommentState(@RequestBody InternalLikeCommentOperateRequest request);
}

