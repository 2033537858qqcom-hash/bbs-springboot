package com.liang.bbs.user.service.client;

import com.liang.bbs.article.facade.dto.ArticleDTO;
import com.liang.bbs.article.facade.dto.ArticleReadDTO;
import com.liang.bbs.article.facade.dto.InternalArticleIdsRequest;
import com.liang.bbs.article.facade.dto.InternalBaseArticleIdsRequest;
import com.liang.bbs.article.facade.dto.InternalTimeRangeRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(
        contextId = "userArticleArticleClient",
        name = "${local.services.bbs-article.name:ns-bbs-article}"
,
        path = "/internal/article/article"
)
public interface ArticleArticleClient {

    @PostMapping("/pass-all")
    List<ArticleDTO> getPassAll(@RequestBody InternalTimeRangeRequest request);

    @PostMapping("/by-ids")
    List<ArticleDTO> getByIds(@RequestBody InternalArticleIdsRequest request);

    @PostMapping("/base-by-ids")
    List<ArticleDTO> getBaseByIds(@RequestBody InternalBaseArticleIdsRequest request);

    @PostMapping("/user-read-count")
    List<ArticleReadDTO> getUserReadCount(@RequestBody List<Long> userIds);

    @GetMapping("/by-user-id/{userId}")
    List<ArticleDTO> getByUserId(@PathVariable("userId") Long userId);
}
