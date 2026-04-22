package com.liang.bbs.article.service.controller.internal;

import com.github.pagehelper.PageInfo;
import com.liang.bbs.article.facade.dto.ArticleDTO;
import com.liang.bbs.article.facade.dto.ArticleReadDTO;
import com.liang.bbs.article.facade.dto.ArticleCheckCountDTO;
import com.liang.bbs.article.facade.dto.ArticleCountDTO;
import com.liang.bbs.article.facade.dto.InternalArticleIdsRequest;
import com.liang.bbs.article.facade.dto.InternalBaseArticleIdsRequest;
import com.liang.bbs.article.facade.dto.InternalArticleCountRequest;
import com.liang.bbs.article.facade.dto.InternalArticleCreateUpdateRequest;
import com.liang.bbs.article.facade.dto.InternalArticleDeleteRequest;
import com.liang.bbs.article.facade.dto.InternalArticleListRequest;
import com.liang.bbs.article.facade.dto.InternalArticleStateRequest;
import com.liang.bbs.article.facade.dto.InternalArticleTopRequest;
import com.liang.bbs.article.facade.dto.InternalBinaryUploadRequest;
import com.liang.bbs.article.facade.dto.InternalLikeSearchRequest;
import com.liang.bbs.article.facade.dto.InternalTimeRangeRequest;
import com.liang.bbs.article.facade.dto.TotalDTO;
import com.liang.bbs.article.facade.server.ArticleService;
import com.liang.bbs.common.enums.ArticleStateEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/internal/article/article")
public class InternalArticleController {

    @Autowired
    private ArticleService articleService;

    @PostMapping("/pass-all")
    public List<ArticleDTO> getPassAll(@RequestBody InternalTimeRangeRequest request) {
        return articleService.getPassAll(request.getStartTime(), request.getEndTime());
    }

    @PostMapping("/list")
    public PageInfo<ArticleDTO> getList(@RequestBody InternalArticleListRequest request) {
        return articleService.getList(request.getArticleSearchDTO(), request.getCurrentUser(), request.getArticleStateEnum());
    }

    @PostMapping("/pending-review")
    public PageInfo<ArticleDTO> getPendingReviewArticles(@RequestBody InternalArticleListRequest request) {
        return articleService.getPendingReviewArticles(request.getArticleSearchDTO(), request.getCurrentUser());
    }

    @PostMapping("/disabled")
    public PageInfo<ArticleDTO> getDisabledArticles(@RequestBody InternalArticleListRequest request) {
        return articleService.getDisabledArticles(request.getArticleSearchDTO(), request.getCurrentUser());
    }

    @PostMapping("/update-state")
    public Boolean updateState(@RequestBody InternalArticleStateRequest request) {
        return articleService.updateState(request.getArticleDTO(), request.getCurrentUser());
    }

    @PostMapping("/likes-article")
    public PageInfo<ArticleDTO> getLikesArticle(@RequestBody InternalLikeSearchRequest request) {
        return articleService.getLikesArticle(request.getLikeSearchDTO(), request.getCurrentUser());
    }

    @PostMapping("/by-ids")
    public List<ArticleDTO> getByIds(@RequestBody InternalArticleIdsRequest request) {
        return articleService.getByIds(request.getIds(), request.getIsPv(), request.getCurrentUser());
    }

    @PostMapping("/base-by-ids")
    public List<ArticleDTO> getBaseByIds(@RequestBody InternalBaseArticleIdsRequest request) {
        return articleService.getBaseByIds(request.getIds(), request.getArticleStateEnum());
    }

    @PostMapping("/user-read-count")
    public List<ArticleReadDTO> getUserReadCount(@RequestBody List<Long> userIds) {
        return articleService.getUserReadCount(userIds);
    }

    @GetMapping("/by-user-id/{userId}")
    public List<ArticleDTO> getByUserId(@PathVariable Long userId) {
        return articleService.getByUserId(userId);
    }

    @PostMapping("/create")
    public Boolean create(@RequestBody InternalArticleCreateUpdateRequest request) {
        if (request.getBytes() == null) {
            return articleService.create(request.getArticleDTO(), request.getLabelIds(), request.getCurrentUser());
        }
        return articleService.create(request.getBytes(), request.getSourceFileName(), request.getArticleDTO(), request.getLabelIds(), request.getCurrentUser());
    }

    @PostMapping("/update")
    public Boolean update(@RequestBody InternalArticleCreateUpdateRequest request) {
        if (request.getBytes() == null) {
            return articleService.update(request.getArticleDTO(), request.getLabelIds(), request.getCurrentUser());
        }
        return articleService.update(request.getBytes(), request.getSourceFileName(), request.getArticleDTO(), request.getLabelIds(), request.getCurrentUser());
    }

    @PostMapping("/upload-picture")
    public String uploadPicture(@RequestBody InternalBinaryUploadRequest request) {
        return articleService.uploadPicture(request.getBytes(), request.getSourceFileName());
    }

    @GetMapping("/article-comment-visit-total")
    public TotalDTO getArticleCommentVisitTotal() {
        return articleService.getArticleCommentVisitTotal();
    }

    @PostMapping("/count-by-id")
    public ArticleCountDTO getCountById(@RequestBody InternalArticleCountRequest request) {
        return articleService.getCountById(request.getId(), request.getCurrentUser());
    }

    @PostMapping("/top")
    public Boolean articleTop(@RequestBody InternalArticleTopRequest request) {
        return articleService.articleTop(request.getId(), request.getTop(), request.getCurrentUser());
    }

    @PostMapping("/delete")
    public Boolean delete(@RequestBody InternalArticleDeleteRequest request) {
        return articleService.delete(request.getId(), request.getCurrentUser());
    }

    @GetMapping("/user-article-count/{createUser}/{articleState}")
    public Long getUserArticleCount(@PathVariable Long createUser, @PathVariable String articleState) {
        return articleService.getUserArticleCount(createUser, "null".equals(articleState) ? null : ArticleStateEnum.valueOf(articleState));
    }

    @GetMapping("/article-check-count")
    public ArticleCheckCountDTO getArticleCheckCount(String title) {
        return articleService.getArticleCheckCount(title);
    }
}
