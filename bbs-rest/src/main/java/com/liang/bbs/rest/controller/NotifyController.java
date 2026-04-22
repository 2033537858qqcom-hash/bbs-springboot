package com.liang.bbs.rest.controller;

import com.github.pagehelper.PageInfo;
import com.liang.bbs.rest.client.NotifyServiceClient;
import com.liang.bbs.rest.config.swagger.ApiVersion;
import com.liang.bbs.rest.config.swagger.ApiVersionConstant;
import com.liang.manage.auth.facade.dto.notify.NotifyOutDTO;
import com.liang.manage.auth.facade.dto.notify.NotifySearchDTO;
import com.liang.nansheng.common.auth.UserContextUtils;
import com.liang.nansheng.common.auth.UserSsoDTO;
import com.liang.nansheng.common.web.basic.ResponseResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 */
@Slf4j
@RestController
@RequestMapping("/bbs/notify/")
@Tag(name = "通知管理")
public class NotifyController {
    @Autowired
    private NotifyServiceClient notifyService;

    /**
     * 閸旂姳绗侤RequestParam閿涘ype韫囧懘銆忕憰浣风炊閸?
     *
     * @param type
     * @return
     */
    @PostMapping("haveRead")
    @Operation(summary = "通知是否已读")
    @ApiVersion(group = ApiVersionConstant.V_300)
    public ResponseResult<Boolean> haveRead(Integer type) {
        UserSsoDTO currentUser = UserContextUtils.currentUser();
        return ResponseResult.success(notifyService.haveRead(currentUser, type));
    }

    @PostMapping("markRead")
    @Operation(summary = "批量标记已读")
    @ApiVersion(group = ApiVersionConstant.V_300)
    public ResponseResult<Boolean> markRead(@RequestBody List<Integer> notifyIds) {
        UserSsoDTO currentUser = UserContextUtils.currentUser();
        return ResponseResult.success(notifyService.markRead(notifyIds, currentUser));
    }

    @GetMapping("getList")
    @Operation(summary = "获取通知列表")
    @ApiVersion(group = ApiVersionConstant.V_300)
    public ResponseResult<PageInfo<NotifyOutDTO>> getList(NotifySearchDTO notifySearchDTO) {
        UserSsoDTO currentUser = UserContextUtils.currentUser();
        return ResponseResult.success(notifyService.getList(notifySearchDTO, currentUser));
    }

}

