package com.liang.bbs.article.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(
        contextId = "articleVisitServiceClient",
        name = "${local.services.manage-auth.name:ns-manage-auth}",
        url = "${local.services.manage-auth.url}",
        path = "/visit"
)
public interface VisitServiceClient {

    @GetMapping("/total")
    Long getTotal();
}

