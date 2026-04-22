package com.liang.bbs.rest.controller;

import com.github.pagehelper.PageInfo;
import com.liang.bbs.article.facade.dto.CommentDTO;
import com.liang.bbs.article.facade.dto.CommentSearchDTO;
import com.liang.bbs.article.facade.dto.InternalCommentOperateRequest;
import com.liang.bbs.article.facade.dto.InternalCommentQueryRequest;
import com.liang.bbs.rest.config.login.NoNeedLogin;
import com.liang.bbs.rest.config.swagger.ApiVersion;
import com.liang.bbs.rest.config.swagger.ApiVersionConstant;
import com.liang.bbs.rest.client.ArticleCommentClient;
import com.liang.nansheng.common.auth.UserContextUtils;
import com.liang.nansheng.common.auth.UserSsoDTO;
import com.liang.nansheng.common.web.basic.ResponseResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 */
@Slf4j
@RestController
@RequestMapping("/bbs/comment/")
@Tag(name = "API")
public class CommentController {
    @Autowired
    private ArticleCommentClient articleCommentClient;

    @NoNeedLogin
    @GetMapping("getCommentByArticleId")
@Operation(summary = "API")
    @ApiVersion(group = ApiVersionConstant.V_300)
    public ResponseResult<List<CommentDTO>> getCommentByArticleId(CommentSearchDTO commentSearchDTO) {
        UserSsoDTO currentUser = UserContextUtils.currentUser();
        InternalCommentQueryRequest request = new InternalCommentQueryRequest();
        request.setCommentSearchDTO(commentSearchDTO);
        request.setCurrentUser(currentUser);
        return ResponseResult.success(articleCommentClient.getCommentByArticleId(request));
    }

    @NoNeedLogin
    @GetMapping("getLatestComment")
@Operation(summary = "API")
    @ApiVersion(group = ApiVersionConstant.V_300)
    public ResponseResult<PageInfo<CommentDTO>> getLatestComment(CommentSearchDTO commentSearchDTO) {
        InternalCommentQueryRequest request = new InternalCommentQueryRequest();
        request.setCommentSearchDTO(commentSearchDTO);
        return ResponseResult.success(articleCommentClient.getLatestComment(request));
    }

    @PostMapping("create")
@Operation(summary = "API")
    @ApiVersion(group = ApiVersionConstant.V_300)
    public ResponseResult<Boolean> create(@RequestBody CommentDTO commentDTO) {
        UserSsoDTO currentUser = UserContextUtils.currentUser();
        InternalCommentOperateRequest request = new InternalCommentOperateRequest();
        request.setCommentDTO(commentDTO);
        request.setCurrentUser(currentUser);
        return ResponseResult.success(articleCommentClient.create(request));
    }

    @PostMapping("delete/{commentId}")
@Operation(summary = "API")
    @ApiVersion(group = ApiVersionConstant.V_300)
    public ResponseResult<Boolean> delete(@PathVariable Integer commentId) {
        return ResponseResult.success(articleCommentClient.delete(commentId));
    }

}

