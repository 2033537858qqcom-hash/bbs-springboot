package com.liang.bbs.article.service.client;

import com.liang.bbs.user.facade.dto.FollowDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        contextId = "articleUserFollowClient",
        name = "${local.services.bbs-user.name:ns-bbs-user}",
        url = "${local.services.bbs-user.url}",
        path = "/internal/user/follow"
)
public interface UserFollowClient {

    @GetMapping("/relation/{fromUser}/{toUser}/{isAll}")
    FollowDTO getByFromToUser(@PathVariable("fromUser") Long fromUser,
                              @PathVariable("toUser") Long toUser,
                              @PathVariable("isAll") Boolean isAll);
}

