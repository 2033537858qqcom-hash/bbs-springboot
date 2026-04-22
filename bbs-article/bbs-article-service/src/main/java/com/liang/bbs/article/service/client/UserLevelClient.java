package com.liang.bbs.article.service.client;

import com.liang.bbs.user.facade.dto.UserLevelDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(
        contextId = "articleUserLevelClient",
        name = "${local.services.bbs-user.name:ns-bbs-user}"
,
        path = "/internal/user/user-level"
)
public interface UserLevelClient {

    @GetMapping("/by-user-id/{userId}")
    List<UserLevelDTO> getByUserId(@PathVariable("userId") Long userId);

    @PostMapping("/by-user-ids")
    List<UserLevelDTO> getByUserIds(@RequestBody List<Long> userIds);
}
