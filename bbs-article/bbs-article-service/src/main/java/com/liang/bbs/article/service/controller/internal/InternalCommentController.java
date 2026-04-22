package com.liang.bbs.article.service.controller.internal;

import com.github.pagehelper.PageInfo;
import com.liang.bbs.article.facade.dto.CommentDTO;
import com.liang.bbs.article.facade.dto.InternalCommentOperateRequest;
import com.liang.bbs.article.facade.dto.InternalCommentQueryRequest;
import com.liang.bbs.article.facade.dto.InternalTimeRangeRequest;
import com.liang.bbs.article.facade.server.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/internal/article/comment")
public class InternalCommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/article-list")
    public List<CommentDTO> getCommentByArticleId(@RequestBody InternalCommentQueryRequest request) {
        return commentService.getCommentByArticleId(request.getCommentSearchDTO(), request.getCurrentUser());
    }

    @PostMapping("/latest")
    public PageInfo<CommentDTO> getLatestComment(@RequestBody InternalCommentQueryRequest request) {
        return commentService.getLatestComment(request.getCommentSearchDTO());
    }

    @PostMapping("/create")
    public Boolean create(@RequestBody InternalCommentOperateRequest request) {
        return commentService.create(request.getCommentDTO(), request.getCurrentUser());
    }

    @PostMapping("/delete/{commentId}")
    public Boolean delete(@PathVariable Integer commentId) {
        return commentService.delete(commentId);
    }

    @PostMapping("/all-article-comments")
    public List<CommentDTO> getAllArticleComment(@RequestBody InternalTimeRangeRequest request) {
        return commentService.getAllArticleComment(request.getStartTime(), request.getEndTime());
    }

    @PostMapping("/all-comment-reply")
    public List<CommentDTO> getAllCommentReply(@RequestBody InternalTimeRangeRequest request) {
        return commentService.getAllCommentReply(request.getStartTime(), request.getEndTime());
    }

    @GetMapping("/article-id/{commentId}")
    public Integer getArticleIdByCommentId(@PathVariable Integer commentId) {
        return commentService.getArticleIdByCommentId(commentId);
    }

    @GetMapping("/by-id/{commentId}")
    public CommentDTO getById(@PathVariable Integer commentId) {
        return commentService.getById(commentId);
    }
}
