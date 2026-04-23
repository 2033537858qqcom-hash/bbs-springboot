package com.liang.bbs.rest.client;

import com.github.pagehelper.PageInfo;
import com.liang.bbs.user.facade.dto.FollowCountDTO;
import com.liang.bbs.user.facade.dto.FollowDTO;
import com.liang.bbs.user.facade.dto.InternalFollowQueryRequest;
import com.liang.bbs.user.facade.dto.InternalFollowStateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        contextId = "userFollowClient",
        name = "${local.services.bbs-user.name:ns-bbs-user}", url = "${local.services.bbs-user.url}"
,
        path = "/internal/user/follow"
)
public interface UserFollowClient {

    @PostMapping("/users")
    PageInfo<FollowDTO> getFollowUsers(@RequestBody InternalFollowQueryRequest request);

    @PostMapping("/state")
    Boolean updateFollowState(@RequestBody InternalFollowStateRequest request);

    @GetMapping("/count/{userId}")
    FollowCountDTO getFollowCount(@PathVariable("userId") Long userId);
}

