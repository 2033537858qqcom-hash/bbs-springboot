package com.liang.bbs.rest.client;

import com.liang.bbs.user.facade.dto.InternalLikeOperateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        contextId = "userLikeClient",
        name = "${local.services.bbs-user.name:ns-bbs-user}"
,
        path = "/internal/user/like"
)
public interface UserLikeClient {

    @PostMapping("/state")
    Boolean updateLikeState(@RequestBody InternalLikeOperateRequest request);

    @GetMapping("/user-the-like-count/{userId}")
    Long getUserTheLikeCount(@PathVariable("userId") Long userId);
}
