package com.liang.bbs.article.service.client;

import com.github.pagehelper.PageInfo;
import com.liang.bbs.user.facade.dto.LikeDTO;
import com.liang.bbs.user.facade.dto.LikeSearchDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(
        contextId = "articleUserLikeClient",
        name = "${local.services.bbs-user.name:ns-bbs-user}",
        url = "${local.services.bbs-user.url}",
        path = "/internal/user/like"
)
public interface UserLikeClient {

    @PostMapping("/article-by-user")
    PageInfo<LikeDTO> getArticleByUserId(@RequestBody LikeSearchDTO likeSearchDTO);

    @PostMapping("/count")
    Long getLikeCountArticle(@RequestBody List<Integer> articleIds);

    @GetMapping("/is-like/{articleId}/{userId}")
    Boolean isLike(@PathVariable("articleId") Integer articleId, @PathVariable("userId") Long userId);
}

