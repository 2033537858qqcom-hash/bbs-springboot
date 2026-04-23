package com.liang.bbs.rest.client;

import com.github.pagehelper.PageInfo;
import com.liang.bbs.article.facade.dto.ArticleCheckCountDTO;
import com.liang.bbs.article.facade.dto.ArticleCountDTO;
import com.liang.bbs.article.facade.dto.ArticleDTO;
import com.liang.bbs.article.facade.dto.InternalArticleCountRequest;
import com.liang.bbs.article.facade.dto.InternalArticleCreateUpdateRequest;
import com.liang.bbs.article.facade.dto.InternalArticleDeleteRequest;
import com.liang.bbs.article.facade.dto.InternalArticleIdsRequest;
import com.liang.bbs.article.facade.dto.InternalArticleListRequest;
import com.liang.bbs.article.facade.dto.InternalArticleStateRequest;
import com.liang.bbs.article.facade.dto.InternalArticleTopRequest;
import com.liang.bbs.article.facade.dto.InternalBinaryUploadRequest;
import com.liang.bbs.article.facade.dto.InternalLikeSearchRequest;
import com.liang.bbs.article.facade.dto.TotalDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(
        contextId = "articleArticleClient",
        name = "${local.services.bbs-article.name:ns-bbs-article}", url = "${local.services.bbs-article.url}"
,
        path = "/internal/article/article"
)
public interface ArticleArticleClient {

    @PostMapping("/list")
    PageInfo<ArticleDTO> getList(@RequestBody InternalArticleListRequest request);

    @PostMapping("/pending-review")
    PageInfo<ArticleDTO> getPendingReviewArticles(@RequestBody InternalArticleListRequest request);

    @PostMapping("/disabled")
    PageInfo<ArticleDTO> getDisabledArticles(@RequestBody InternalArticleListRequest request);

    @PostMapping("/update-state")
    Boolean updateState(@RequestBody InternalArticleStateRequest request);

    @PostMapping("/likes-article")
    PageInfo<ArticleDTO> getLikesArticle(@RequestBody InternalLikeSearchRequest request);

    @PostMapping("/by-ids")
    List<ArticleDTO> getByIds(@RequestBody InternalArticleIdsRequest request);

    @PostMapping("/create")
    Boolean create(@RequestBody InternalArticleCreateUpdateRequest request);

    @PostMapping("/update")
    Boolean update(@RequestBody InternalArticleCreateUpdateRequest request);

    @PostMapping("/upload-picture")
    String uploadPicture(@RequestBody InternalBinaryUploadRequest request);

    @GetMapping("/article-comment-visit-total")
    TotalDTO getArticleCommentVisitTotal();

    @PostMapping("/count-by-id")
    ArticleCountDTO getCountById(@RequestBody InternalArticleCountRequest request);

    @PostMapping("/top")
    Boolean articleTop(@RequestBody InternalArticleTopRequest request);

    @PostMapping("/delete")
    Boolean delete(@RequestBody InternalArticleDeleteRequest request);

    @GetMapping("/user-article-count/{createUser}/{articleState}")
    Long getUserArticleCount(@PathVariable("createUser") Long createUser, @PathVariable("articleState") String articleState);

    @GetMapping("/article-check-count")
    ArticleCheckCountDTO getArticleCheckCount(@RequestParam("title") String title);
}

