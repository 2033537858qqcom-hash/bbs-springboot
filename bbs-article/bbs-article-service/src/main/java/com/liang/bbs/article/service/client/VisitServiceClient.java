package com.liang.bbs.article.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(
        contextId = "articleVisitServiceClient",
        url = "${local.services.manage-auth.url}",  // 本地认证服务直连
        path = "/visit"
)
public interface VisitServiceClient {

    @GetMapping("/total")
    Long getTotal();
}
