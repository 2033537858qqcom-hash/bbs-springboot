package com.liang.bbs.user.service.controller.internal;

import com.github.pagehelper.PageInfo;
import com.liang.bbs.user.facade.dto.FollowCountDTO;
import com.liang.bbs.user.facade.dto.FollowDTO;
import com.liang.bbs.user.facade.dto.FollowSearchDTO;
import com.liang.bbs.user.facade.dto.InternalFollowQueryRequest;
import com.liang.bbs.user.facade.dto.InternalFollowStateRequest;
import com.liang.bbs.user.facade.server.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/user/follow")
public class InternalFollowController {

    @Autowired
    private FollowService followService;

    @PostMapping("/users")
    public PageInfo<FollowDTO> getFollowUsers(@RequestBody InternalFollowQueryRequest request) {
        return followService.getFollowUsers(request.getFollowSearchDTO(), request.getCurrentUser());
    }

    @PostMapping("/state")
    public Boolean updateFollowState(@RequestBody InternalFollowStateRequest request) {
        return followService.updateFollowState(request.getFromUser(), request.getToUser());
    }

    @GetMapping("/count/{userId}")
    public FollowCountDTO getFollowCount(@PathVariable Long userId) {
        return followService.getFollowCount(userId);
    }

    @GetMapping("/relation/{fromUser}/{toUser}/{isAll}")
    public FollowDTO getByFromToUser(@PathVariable Long fromUser, @PathVariable Long toUser, @PathVariable Boolean isAll) {
        return followService.getByFromToUser(fromUser, toUser, isAll);
    }
}
