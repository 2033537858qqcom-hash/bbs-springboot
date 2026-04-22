package com.liang.bbs.rest.client;

import com.github.pagehelper.PageInfo;
import com.liang.manage.auth.facade.dto.notify.NotifyOutDTO;
import com.liang.manage.auth.facade.dto.notify.NotifySearchDTO;
import com.liang.nansheng.common.auth.UserSsoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(
        contextId = "restNotifyServiceClient",
        name = "${local.services.manage-auth.name:ns-manage-auth}",
        path = "/notify"
)
public interface NotifyServiceClient {

    @PostMapping("/have-read")
    Boolean haveRead(@SpringQueryMap UserSsoDTO currentUser, @RequestParam("type") Integer type);

    @PostMapping("/mark-read")
    Boolean markRead(@RequestBody List<Integer> notifyIds, @SpringQueryMap UserSsoDTO currentUser);

    @GetMapping("/list")
    PageInfo<NotifyOutDTO> getList(@SpringQueryMap NotifySearchDTO notifySearchDTO, @SpringQueryMap UserSsoDTO currentUser);

    @GetMapping("/not-read-count")
    Integer getNotReadNotifyCount(@RequestParam("userId") Long userId, @RequestParam("type") Integer type);
}
