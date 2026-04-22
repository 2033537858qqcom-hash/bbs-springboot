package com.liang.bbs.user.service.controller.internal;

import com.github.pagehelper.PageInfo;
import com.liang.bbs.user.facade.dto.InternalUserLevelCreateRequest;
import com.liang.bbs.user.facade.dto.InternalUserLevelHotAuthorsRequest;
import com.liang.bbs.user.facade.dto.InternalUserLevelUserInfoRequest;
import com.liang.bbs.user.facade.dto.UserForumDTO;
import com.liang.bbs.user.facade.dto.UserLevelDTO;
import com.liang.bbs.user.facade.server.UserLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/internal/user/user-level")
public class InternalUserLevelController {

    @Autowired
    private UserLevelService userLevelService;

    @PostMapping("/create")
    public Boolean create(@RequestBody InternalUserLevelCreateRequest request) {
        return userLevelService.create(request.getUserId());
    }

    @GetMapping("/by-user-id/{userId}")
    public List<UserLevelDTO> getByUserId(@PathVariable Long userId) {
        return userLevelService.getByUserId(userId);
    }

    @PostMapping("/by-user-ids")
    public List<UserLevelDTO> getByUserIds(@RequestBody List<Long> userIds) {
        return userLevelService.getByUserIds(userIds);
    }

    @PostMapping("/hot-authors")
    public PageInfo<UserForumDTO> getHotAuthorsList(@RequestBody InternalUserLevelHotAuthorsRequest request) {
        return userLevelService.getHotAuthorsList(request.getUserSearchDTO(), request.getCurrentUser());
    }

    @PostMapping("/user-info")
    public UserForumDTO getUserInfo(@RequestBody InternalUserLevelUserInfoRequest request) {
        return userLevelService.getUserInfo(request.getUserId(), request.getCurrentUser());
    }
}
