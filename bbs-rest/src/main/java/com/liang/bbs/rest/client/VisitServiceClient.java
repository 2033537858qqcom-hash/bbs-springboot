package com.liang.bbs.rest.client;

import com.liang.manage.auth.facade.dto.visit.VisitDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        contextId = "restVisitServiceClient",
        url = "${local.services.manage-auth.url}",  // 本地认证服务直连
        path = "/visit"
)
public interface VisitServiceClient {

    @PostMapping("/create")
    Boolean create(@RequestBody VisitDTO visitDTO);

    @GetMapping("/total")
    Long getTotal();
}
