package com.liang.local.auth.controller;

import com.github.pagehelper.PageInfo;
import com.liang.manage.auth.facade.dto.notify.NotifyOutDTO;
import com.liang.manage.auth.facade.dto.notify.NotifySearchDTO;
import com.liang.nansheng.common.auth.UserSsoDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 本地通知Controller
 */
@Slf4j
@RestController
@RequestMapping("/notify")
public class NotifyController {

    /**
     * 标记已读
     */
    @PostMapping("/have-read")
    public Boolean haveRead(@RequestHeader(value = "X-Current-User", required = false) String currentUserJson,
                           @RequestParam("type") Integer type) {
        log.info("标记通知已读, type={}", type);
        return true;
    }

    /**
     * 批量标记已读
     */
    @PostMapping("/mark-read")
    public Boolean markRead(@RequestBody List<Integer> notifyIds,
                           @RequestHeader(value = "X-Current-User", required = false) String currentUserJson) {
        log.info("批量标记通知已读, ids={}", notifyIds);
        return true;
    }

    /**
     * 获取通知列表
     */
    @GetMapping("/list")
    public PageInfo<NotifyOutDTO> getList(@ModelAttribute NotifySearchDTO notifySearchDTO,
                                         @RequestHeader(value = "X-Current-User", required = false) String currentUserJson) {
        log.info("获取通知列表");
        // 返回空列表
        return new PageInfo<>(new ArrayList<>());
    }

    /**
     * 获取未读通知数量
     */
    @GetMapping("/not-read-count")
    public Integer getNotReadNotifyCount(@RequestParam("userId") Long userId,
                                        @RequestParam("type") Integer type) {
        // 返回0表示没有未读通知
        return 0;
    }
}
