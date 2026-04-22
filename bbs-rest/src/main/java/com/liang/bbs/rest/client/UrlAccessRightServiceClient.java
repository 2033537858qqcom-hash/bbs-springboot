package com.liang.bbs.rest.client;

import com.liang.nansheng.common.auth.UserSsoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        contextId = "restUrlAccessRightServiceClient",
        name = "${local.services.manage-auth.name:ns-manage-auth}",
        path = "/url-access-right"
)
public interface UrlAccessRightServiceClient {

    @GetMapping("/check")
    Boolean checkUrlAccess(@SpringQueryMap UserSsoDTO currentUser,
                           @RequestParam("uri") String uri,
                           @RequestParam("attribute") String attribute);
}
