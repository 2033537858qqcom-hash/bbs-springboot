package com.liang.bbs.rest.client;

import com.github.pagehelper.PageInfo;
import com.liang.bbs.user.facade.dto.DynamicDTO;
import com.liang.bbs.user.facade.dto.InternalDynamicListRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        contextId = "userDynamicClient",
        name = "${local.services.bbs-user.name:ns-bbs-user}", url = "${local.services.bbs-user.url}"
,
        path = "/internal/user/dynamic"
)
public interface UserDynamicClient {

    @PostMapping("/list")
    PageInfo<DynamicDTO> getList(@RequestBody InternalDynamicListRequest request);
}

