package com.liang.bbs.user.service.client;

import com.liang.manage.auth.facade.dto.user.UserDTO;
import com.liang.manage.auth.facade.dto.user.UserListDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(
        contextId = "userUserServiceClient",
        name = "${local.services.manage-auth.name:ns-manage-auth}",
        url = "${local.services.manage-auth.url}",
        path = "/user"
)
public interface UserServiceClient {

    @GetMapping("/by-id")
    UserDTO getById(@RequestParam("id") Long id);

    @GetMapping("/all-list")
    List<UserListDTO> getAllList();

    @GetMapping("/by-ids")
    List<UserDTO> getByIds(@RequestParam("userIds") List<Long> userIds);
}

