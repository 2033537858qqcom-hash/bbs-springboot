package com.liang.bbs.article.service.client;

import com.liang.manage.auth.facade.dto.user.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(
        contextId = "articleUserServiceClient",
        name = "${local.services.manage-auth.name:ns-manage-auth}",
        path = "/user"
)
public interface UserServiceClient {

    @GetMapping("/by-ids")
    List<UserDTO> getByIds(@RequestParam("userIds") List<Long> userIds);
}
