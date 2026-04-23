package com.liang.bbs.rest.client;

import com.github.pagehelper.PageInfo;
import com.liang.bbs.user.facade.dto.InternalUserLevelCreateRequest;
import com.liang.bbs.user.facade.dto.InternalUserLevelHotAuthorsRequest;
import com.liang.bbs.user.facade.dto.InternalUserLevelUserInfoRequest;
import com.liang.bbs.user.facade.dto.UserForumDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        contextId = "restUserLevelClient",
        name = "${local.services.bbs-user.name:ns-bbs-user}", url = "${local.services.bbs-user.url}"
,
        path = "/internal/user/user-level"
)
public interface UserLevelClient {

    @PostMapping("/create")
    Boolean create(@RequestBody InternalUserLevelCreateRequest request);

    @PostMapping("/hot-authors")
    PageInfo<UserForumDTO> getHotAuthorsList(@RequestBody InternalUserLevelHotAuthorsRequest request);

    @PostMapping("/user-info")
    UserForumDTO getUserInfo(@RequestBody InternalUserLevelUserInfoRequest request);
}

