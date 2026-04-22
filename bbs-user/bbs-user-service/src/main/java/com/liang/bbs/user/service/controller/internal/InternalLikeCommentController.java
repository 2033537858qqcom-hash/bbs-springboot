package com.liang.bbs.user.service.controller.internal;

import com.liang.bbs.user.facade.dto.InternalLikeCommentOperateRequest;
import com.liang.bbs.user.facade.server.LikeCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/user/like-comment")
public class InternalLikeCommentController {

    @Autowired
    private LikeCommentService likeCommentService;

    @PostMapping("/state")
    public Boolean updateLikeCommentState(@RequestBody InternalLikeCommentOperateRequest request) {
        return likeCommentService.updateLikeCommentState(request.getCommentId(), request.getCurrentUser());
    }

    @GetMapping("/count/{commentId}")
    public Long getLikeCountCommentId(@PathVariable Integer commentId) {
        return likeCommentService.getLikeCountCommentId(commentId);
    }

    @GetMapping("/is-like/{commentId}/{userId}")
    public Boolean isLike(@PathVariable Integer commentId, @PathVariable Long userId) {
        return likeCommentService.isLike(commentId, userId);
    }
}
