package com.liang.bbs.user.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.liang.bbs.article.facade.dto.ArticleDTO;
import com.liang.bbs.article.facade.dto.CommentDTO;
import com.liang.bbs.article.facade.dto.InternalArticleIdsRequest;
import com.liang.bbs.article.facade.dto.InternalTimeRangeRequest;
import com.liang.bbs.common.enums.DynamicTypeEnum;
import com.liang.bbs.user.facade.dto.DynamicDTO;
import com.liang.bbs.user.facade.dto.FollowDTO;
import com.liang.bbs.user.facade.dto.LikeCommentDTO;
import com.liang.bbs.user.facade.dto.LikeDTO;
import com.liang.bbs.user.facade.server.DynamicService;
import com.liang.bbs.user.facade.server.FollowService;
import com.liang.bbs.user.facade.server.LikeCommentService;
import com.liang.bbs.user.facade.server.LikeService;
import com.liang.bbs.user.persistence.entity.DynamicPo;
import com.liang.bbs.user.persistence.entity.DynamicPoExample;
import com.liang.bbs.user.persistence.mapper.DynamicPoMapper;
import com.liang.bbs.user.service.client.ArticleArticleClient;
import com.liang.bbs.user.service.client.ArticleCommentClient;
import com.liang.bbs.user.service.client.UserServiceClient;
import com.liang.bbs.user.service.mapstruct.DynamicMS;
import com.liang.manage.auth.facade.dto.user.UserDTO;
import com.liang.nansheng.common.enums.ResponseCode;
import com.liang.nansheng.common.utils.CommonUtils;
import com.liang.nansheng.common.web.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 */
@Slf4j
@Service
public class DynamicServiceImpl implements DynamicService {
    @Autowired
    private DynamicPoMapper dynamicPoMapper;

    @Autowired
    private ArticleArticleClient articleArticleClient;

    @Autowired
    private ArticleCommentClient articleCommentClient;

    @Autowired
    private UserServiceClient userService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private LikeCommentService likeCommentService;

    @Autowired
    private FollowService followService;

    /**
     * 閼惧嘲褰囬悽銊﹀煕閻ㄥ嫬濮╅幀浣蜂繆閹?
     *
     * @param userId
     * @param currentPage
     * @param pageSize
     * @return
     */
    @Override
    public PageInfo<DynamicDTO> getByUserId(Long userId, Integer currentPage, Integer pageSize) {
        DynamicPoExample example = new DynamicPoExample();
        example.createCriteria().andUserIdEqualTo(userId);
        example.setOrderByClause("create_time desc");

        PageHelper.startPage(currentPage, pageSize);
        List<DynamicPo> dynamicPos = dynamicPoMapper.selectByExample(example);
        PageInfo<DynamicDTO> pageInfo = DynamicMS.INSTANCE.toPage(new PageInfo<>(dynamicPos));
        if (CollectionUtils.isNotEmpty(pageInfo.getList())) {
            pageInfo.getList().forEach(dynamicDTO -> {
                UserDTO userDTO = userService.getById(dynamicDTO.getUserId());
                dynamicDTO.setUserName(userDTO.getName());
                dynamicDTO.setPicture(userDTO.getPicture());
                // 閺傚洨鐝烽惄绋垮彠
                if (DynamicTypeEnum.writeArticle.name().equals(dynamicDTO.getType()) ||
                        DynamicTypeEnum.likeArticle.name().equals(dynamicDTO.getType()) ||
                        DynamicTypeEnum.commentArticle.name().equals(dynamicDTO.getType())) {
                    InternalArticleIdsRequest request = new InternalArticleIdsRequest();
                    request.setIds(Collections.singletonList(Integer.parseInt(dynamicDTO.getObjectId())));
                    List<ArticleDTO> articleDTOS = articleArticleClient.getByIds(request);
                    dynamicDTO.setTitle(articleDTOS.get(0).getTitle());
                }
                // 鐠囧嫯顔戦惄绋垮彠
                if (DynamicTypeEnum.likeComment.name().equals(dynamicDTO.getType()) ||
                        DynamicTypeEnum.commentReply.name().equals(dynamicDTO.getType())) {
                    InternalArticleIdsRequest request = new InternalArticleIdsRequest();
                    request.setIds(Collections.singletonList(Integer.parseInt(dynamicDTO.getObjectId())));
                    List<ArticleDTO> articleDTOS = articleArticleClient.getByIds(request);
                    dynamicDTO.setTitle(articleDTOS.get(0).getTitle() + " > " + CommonUtils.html2Text(articleCommentClient.getById(dynamicDTO.getCommentId()).getContent()));
                }
                if (DynamicTypeEnum.followUser.name().equals(dynamicDTO.getType())) {
                    UserDTO userDTO1 = userService.getById(Long.parseLong(dynamicDTO.getObjectId()));
                    dynamicDTO.setTitle(userDTO1.getName());
                }
            });
        }

        return pageInfo;
    }

    /**
     * 閸掓稑缂撻悽銊﹀煕閸斻劍鈧椒淇婇幁?
     *
     * @param dynamicDTO
     * @return
     */
    @Override
    public Boolean create(DynamicDTO dynamicDTO) {
        if (dynamicPoMapper.insertSelective(DynamicMS.INSTANCE.toPo(dynamicDTO)) <= 0) {
            throw BusinessException.build(ResponseCode.OPERATE_FAIL, "新增动态失败");
        }

        return true;
    }

    @Override
    public Boolean verifyExist(DynamicDTO dynamicDTO) {
        DynamicPoExample example = new DynamicPoExample();
        DynamicPoExample.Criteria criteria = example.createCriteria();
        criteria.andTypeEqualTo(dynamicDTO.getType())
                .andUserIdEqualTo(dynamicDTO.getUserId())
                .andObjectIdEqualTo(dynamicDTO.getObjectId());
        if (dynamicDTO.getCommentId() != null) {
            criteria.andCommentIdEqualTo(dynamicDTO.getCommentId());
        }
        List<DynamicPo> dynamicPos = dynamicPoMapper.selectByExample(example);

        return CollectionUtils.isNotEmpty(dynamicPos);
    }

    /**
     * 閸掔娀娅庨悽銊﹀煕閸斻劍鈧椒淇婇幁?
     *
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public Boolean delete(LocalDateTime startTime, LocalDateTime endTime) {
        DynamicPoExample example = new DynamicPoExample();
        if (startTime != null && endTime != null) {
            example.createCriteria().andCreateTimeBetween(startTime, endTime);
        }

        return dynamicPoMapper.deleteByExample(example) > 0;
    }

    /**
     * 閺囧瓨鏌婇幍鈧張澶屾暏閹撮娈戦崝銊︹偓浣蜂繆閹?
     *
     * @return
     */
    @Override
    public void updateAll() {
//        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        LocalDateTime startTime = LocalDateTime.parse("2000-01-01 00:00:00", df);
        LocalDateTime startTime = CommonUtils.getYesterdayStartTime();
        LocalDateTime endTime = CommonUtils.getCurrentEndTime();
        List<DynamicDTO> dynamicDTOS = new ArrayList<>();
        // 閸愭瑦鏋冪粩?
        InternalTimeRangeRequest timeRangeRequest = new InternalTimeRangeRequest();
        timeRangeRequest.setStartTime(startTime);
        timeRangeRequest.setEndTime(endTime);
        List<ArticleDTO> articleDTOS = articleArticleClient.getPassAll(timeRangeRequest);
        if (CollectionUtils.isNotEmpty(articleDTOS)) {
            articleDTOS.forEach(articleDTO -> {
                DynamicDTO dynamicDTO = new DynamicDTO();
                dynamicDTO.setType(DynamicTypeEnum.writeArticle.name());
                dynamicDTO.setUserId(articleDTO.getCreateUser());
                dynamicDTO.setObjectId(String.valueOf(articleDTO.getId()));
                dynamicDTO.setCreateTime(articleDTO.getCreateTime());
                dynamicDTO.setUpdateTime(dynamicDTO.getCreateTime());
                dynamicDTOS.add(dynamicDTO);
            });
        }

        // 閻愮绂愰弬鍥╃彿
        List<LikeDTO> likeDTOS = likeService.getPaasAll(startTime, endTime);
        if (CollectionUtils.isNotEmpty(likeDTOS)) {
            likeDTOS.forEach(likeDTO -> {
                DynamicDTO dynamicDTO = new DynamicDTO();
                dynamicDTO.setType(DynamicTypeEnum.likeArticle.name());
                dynamicDTO.setUserId(likeDTO.getLikeUser());
                dynamicDTO.setObjectId(String.valueOf(likeDTO.getArticleId()));
                dynamicDTO.setCreateTime(likeDTO.getCreateTime());
                dynamicDTO.setUpdateTime(dynamicDTO.getCreateTime());
                dynamicDTOS.add(dynamicDTO);
            });
        }

        // 閻愮绂愮拠鍕啈
        List<LikeCommentDTO> likeCommentDTOS = likeCommentService.getPaasAll(startTime, endTime);
        if (CollectionUtils.isNotEmpty(likeCommentDTOS)) {
            likeCommentDTOS.forEach(likeCommentDTO -> {
                DynamicDTO dynamicDTO = new DynamicDTO();
                dynamicDTO.setType(DynamicTypeEnum.likeComment.name());
                dynamicDTO.setUserId(likeCommentDTO.getLikeUser());
                dynamicDTO.setObjectId(String.valueOf(articleCommentClient.getArticleIdByCommentId(likeCommentDTO.getCommentId())));
                dynamicDTO.setCommentId(likeCommentDTO.getCommentId());
                dynamicDTO.setCreateTime(likeCommentDTO.getCreateTime());
                dynamicDTO.setUpdateTime(dynamicDTO.getCreateTime());
                dynamicDTOS.add(dynamicDTO);
            });
        }

        // 鐠囧嫯顔戦弬鍥╃彿
        List<CommentDTO> commentDTOS = articleCommentClient.getAllArticleComment(timeRangeRequest);
        if (CollectionUtils.isNotEmpty(commentDTOS)) {
            commentDTOS.forEach(commentDTO -> {
                DynamicDTO dynamicDTO = new DynamicDTO();
                dynamicDTO.setType(DynamicTypeEnum.commentArticle.name());
                dynamicDTO.setUserId(commentDTO.getCommentUser());
                dynamicDTO.setObjectId(String.valueOf(commentDTO.getArticleId()));
                dynamicDTO.setCommentId(commentDTO.getId());
                dynamicDTO.setCreateTime(commentDTO.getCreateTime());
                dynamicDTO.setUpdateTime(dynamicDTO.getCreateTime());
                dynamicDTOS.add(dynamicDTO);
            });
        }

        // 鐠囧嫯顔戦崶鐐差槻
        List<CommentDTO> allCommentReply = articleCommentClient.getAllCommentReply(timeRangeRequest);
        if (CollectionUtils.isNotEmpty(allCommentReply)) {
            allCommentReply.forEach(commentDTO -> {
                DynamicDTO dynamicDTO = new DynamicDTO();
                dynamicDTO.setType(DynamicTypeEnum.commentReply.name());
                dynamicDTO.setUserId(commentDTO.getCommentUser());
                dynamicDTO.setObjectId(String.valueOf(commentDTO.getArticleId()));
                dynamicDTO.setCommentId(commentDTO.getPreId());
                dynamicDTO.setCreateTime(commentDTO.getCreateTime());
                dynamicDTO.setUpdateTime(dynamicDTO.getCreateTime());
                dynamicDTOS.add(dynamicDTO);
            });
        }

        // 閸忚櫕鏁?
        List<FollowDTO> followDTOS = followService.getPaasAll(startTime, endTime);
        if (CollectionUtils.isNotEmpty(followDTOS)) {
            followDTOS.forEach(followDTO -> {
                DynamicDTO dynamicDTO = new DynamicDTO();
                dynamicDTO.setType(DynamicTypeEnum.followUser.name());
                dynamicDTO.setUserId(followDTO.getFromUser());
                dynamicDTO.setObjectId(String.valueOf(followDTO.getToUser()));
                dynamicDTO.setCreateTime(followDTO.getCreateTime());
                dynamicDTO.setUpdateTime(dynamicDTO.getCreateTime());
                dynamicDTOS.add(dynamicDTO);
            });
        }

        log.info("dynamicDTOS size: {}", dynamicDTOS.size());
        if (CollectionUtils.isNotEmpty(dynamicDTOS)) {
            dynamicDTOS.forEach(dynamicDTO -> {
                if (!this.verifyExist(dynamicDTO)) {
                    this.create(dynamicDTO);
                }
            });
        }
    }
}


