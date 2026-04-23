package com.liang.manage.auth.service.controller;

import com.github.pagehelper.PageInfo;
import com.liang.manage.auth.facade.dto.notify.NotifyOutDTO;
import com.liang.manage.auth.facade.dto.notify.NotifySearchDTO;
import com.liang.manage.auth.service.store.InMemoryManageAuthStore;
import com.liang.nansheng.common.auth.UserSsoDTO;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/notify")
public class NotifyController {

    private final InMemoryManageAuthStore store;

    public NotifyController(InMemoryManageAuthStore store) {
        this.store = store;
    }

    @PostMapping("/have-read")
    public Boolean haveRead(UserSsoDTO currentUser, @RequestParam("type") Integer type) {
        Long userId = currentUser == null ? null : currentUser.getUserId();
        if (userId == null) {
            return true;
        }
        return store.haveRead(userId, type);
    }

    @PostMapping("/mark-read")
    public Boolean markRead(@RequestBody List<Integer> notifyIds, UserSsoDTO currentUser) {
        Long userId = currentUser == null ? null : currentUser.getUserId();
        if (userId == null) {
            return false;
        }
        return store.markRead(notifyIds == null ? Collections.emptyList() : notifyIds, userId);
    }

    @GetMapping("/list")
    public PageInfo<NotifyOutDTO> getList(NotifySearchDTO notifySearchDTO, UserSsoDTO currentUser) {
        Long userId = currentUser == null ? null : currentUser.getUserId();
        if (userId == null) {
            return new PageInfo<>(Collections.emptyList());
        }
        List<NotifyOutDTO> all = store.listNotify(notifySearchDTO, userId);
        int current = notifySearchDTO == null || notifySearchDTO.getCurrent() == null ? 1 : notifySearchDTO.getCurrent();
        int size = notifySearchDTO == null || notifySearchDTO.getSize() == null ? 10 : notifySearchDTO.getSize();
        int fromIndex = Math.max((current - 1) * size, 0);
        if (fromIndex >= all.size()) {
            return new PageInfo<>(Collections.emptyList());
        }
        int toIndex = Math.min(fromIndex + size, all.size());
        return new PageInfo<>(all.subList(fromIndex, toIndex));
    }

    @GetMapping("/not-read-count")
    public Integer getNotReadNotifyCount(@RequestParam("userId") Long userId, @RequestParam("type") Integer type) {
        return store.notReadCount(userId, type);
    }
}
