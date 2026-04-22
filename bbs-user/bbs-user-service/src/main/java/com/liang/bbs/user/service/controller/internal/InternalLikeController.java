package com.liang.bbs.user.service.controller.internal;

import com.github.pagehelper.PageInfo;
import com.liang.bbs.user.facade.dto.InternalLikeOperateRequest;
import com.liang.bbs.user.facade.dto.LikeDTO;
import com.liang.bbs.user.facade.dto.LikeSearchDTO;
import com.liang.bbs.user.facade.server.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/user/like")
public class InternalLikeController {

    @Autowired
    private LikeService likeService;

    @PostMapping("/state")
    public Boolean updateLikeState(@RequestBody InternalLikeOperateRequest request) {
        return likeService.updateLikeState(request.getArticleId(), request.getCurrentUser());
    }

    @PostMapping("/article-by-user")
    public PageInfo<LikeDTO> getArticleByUserId(@RequestBody LikeSearchDTO likeSearchDTO) {
        return likeService.getArticleByUserId(likeSearchDTO);
    }

    @PostMapping("/count")
    public Long getLikeCountArticle(@RequestBody java.util.List<Integer> articleIds) {
        return likeService.getLikeCountArticle(articleIds);
    }

    @GetMapping("/is-like/{articleId}/{userId}")
    public Boolean isLike(@PathVariable Integer articleId, @PathVariable Long userId) {
        return likeService.isLike(articleId, userId);
    }

    @GetMapping("/user-the-like-count/{userId}")
    public Long getUserTheLikeCount(@PathVariable Long userId) {
        return likeService.getUserTheLikeCount(userId);
    }

    @GetMapping("/user-like-count/{userId}")
    public Long getUserLikeCount(@PathVariable Long userId) {
        return likeService.getUserLikeCount(userId);
    }
}
