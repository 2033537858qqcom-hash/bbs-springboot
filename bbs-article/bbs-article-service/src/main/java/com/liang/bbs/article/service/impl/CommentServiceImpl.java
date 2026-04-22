package com.liang.bbs.article.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.liang.bbs.article.facade.dto.CommentDTO;
import com.liang.bbs.article.facade.dto.CommentSearchDTO;
import com.liang.bbs.article.facade.server.CommentService;
import com.liang.bbs.article.persistence.entity.CommentPo;
import com.liang.bbs.article.persistence.entity.CommentPoExample;
import com.liang.bbs.article.persistence.mapper.CommentPoExMapper;
import com.liang.bbs.article.persistence.mapper.CommentPoMapper;
import com.liang.bbs.article.service.client.UserServiceClient;
import com.liang.bbs.article.service.client.UserLevelClient;
import com.liang.bbs.article.service.client.UserLikeCommentClient;
import com.liang.bbs.article.service.mapstruct.CommentMS;
import com.liang.bbs.article.service.utils.CommentTreeUtils;
import com.liang.bbs.common.enums.SortRuleEnum;
import com.liang.bbs.user.facade.dto.UserLevelDTO;
import com.liang.manage.auth.facade.dto.user.UserDTO;
import com.liang.nansheng.common.auth.UserSsoDTO;
import com.liang.nansheng.common.enums.ResponseCode;
import com.liang.nansheng.common.web.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 */
@Slf4j
@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    private CommentPoMapper commentPoMapper;

    @Autowired
    private CommentPoExMapper commentPoExMapper;

    @Autowired
    private UserServiceClient userService;

    @Autowired
    private UserLevelClient userLevelClient;

    @Autowired
    private UserLikeCommentClient userLikeCommentClient;

    /**
     * 閼惧嘲褰囬弬鍥╃彿閻ㄥ嫯鐦庣拋杞颁繆閹?
     *
     * @param commentSearchDTO
     * @param currentUser
     * @return
     */
    @Override
    public List<CommentDTO> getCommentByArticleId(CommentSearchDTO commentSearchDTO, UserSsoDTO currentUser) {
        CommentPoExample example = new CommentPoExample();
        example.createCriteria().andIsDeletedEqualTo(false)
                .andStateEqualTo(true)
                .andArticleIdEqualTo(commentSearchDTO.getArticleId());
        List<CommentDTO> commentDTOS = CommentMS.INSTANCE.toDTO(commentPoMapper.selectByExample(example));
        if (CollectionUtils.isNotEmpty(commentDTOS)) {
            // 閺嬪嫬缂撶拠鍕啈娣団剝浼?
            buildCommentInfo(commentDTOS, currentUser);
        }

        commentDTOS = CommentTreeUtils.toTree(commentDTOS);
        // 閺堚偓閻戭叀鐦庣拋鐚寸礄閸忓牊瀵滈悙纭呯閺佷即妾锋惔蹇ョ礉閸愬秵瀵滈崶鐐差槻閺佷即妾锋惔蹇ョ礆
        if (SortRuleEnum.hottest.equals(commentSearchDTO.getSortRule())) {
            commentDTOS = commentDTOS.stream()
                    .sorted(Comparator.comparing(CommentDTO::getLikeCount, Comparator.nullsLast(Comparator.reverseOrder()))
                            .thenComparing(CommentDTO::getRepliesCount, Comparator.nullsLast(Comparator.reverseOrder())))
                    .collect(Collectors.toList());
        } else if (SortRuleEnum.newest.equals(commentSearchDTO.getSortRule())) {
            commentDTOS = commentDTOS.stream()
                    .sorted(Comparator.comparing(CommentDTO::getCreateTime, Comparator.nullsLast(Comparator.reverseOrder())))
                    .collect(Collectors.toList());
        }

        return commentDTOS;
    }

    /**
     * 閼惧嘲褰囬幍鈧張澶愨偓姘崇箖鐎光剝鐗抽弬鍥╃彿閻ㄥ嫯鐦庣拋杞颁繆閹?
     *
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public List<CommentDTO> getAllArticleComment(LocalDateTime startTime, LocalDateTime endTime) {
        return CommentMS.INSTANCE.toDTO(commentPoExMapper.getAllArticleComment(startTime, endTime));
    }

    @Override
    public List<CommentDTO> getAllCommentReply(LocalDateTime startTime, LocalDateTime endTime) {
        return CommentMS.INSTANCE.toDTO(commentPoExMapper.getAllCommentReply(startTime, endTime));
    }

    /**
     * 閼惧嘲褰囬張鈧弬鎷岀槑鐠佽桨淇婇幁?
     *
     * @param commentSearchDTO
     * @return
     */
    @Override
    public PageInfo<CommentDTO> getLatestComment(CommentSearchDTO commentSearchDTO) {
        PageHelper.startPage(commentSearchDTO.getCurrentPage(), commentSearchDTO.getPageSize());
        List<CommentPo> commentPos = commentPoExMapper.selectLatestComments(commentSearchDTO.getContent(), commentSearchDTO.getCommentUser());
        PageInfo<CommentDTO> pageInfo = CommentMS.INSTANCE.toPage(new PageInfo<>(commentPos));
        if (CollectionUtils.isNotEmpty(pageInfo.getList())) {
            // 閺嬪嫬缂撶拠鍕啈娣団剝浼?
            buildCommentInfo(pageInfo.getList(), null);
        }

        return pageInfo;
    }

    /**
     * 閼惧嘲褰囬弬鍥╃彿閻ㄥ嫯鐦庣拋鐑樻殶闁?
     *
     * @param articleId
     * @return
     */
    @Override
    public Long getCommentCountByArticle(Integer articleId) {
        CommentPoExample example = new CommentPoExample();
        example.createCriteria().andIsDeletedEqualTo(false)
                .andStateEqualTo(true)
                .andArticleIdEqualTo(articleId);

        return commentPoMapper.countByExample(example);
    }

    /**
     * 閼惧嘲褰囩拠鍕啈閺佷即鍣?
     *
     * @return
     */
    @Override
    public Long getTotal() {
        CommentPoExample example = new CommentPoExample();
        example.createCriteria().andIsDeletedEqualTo(false)
                .andStateEqualTo(true);
        return commentPoMapper.countByExample(example);
    }

    /**
     * 閸掓稑缂撶拠鍕啈
     *
     * @param commentDTO
     * @param currentUser
     * @return
     */
    @Override
    public Boolean create(CommentDTO commentDTO, UserSsoDTO currentUser) {
        LocalDateTime now = LocalDateTime.now();
        commentDTO.setIsDeleted(false);
        commentDTO.setState(true);
        commentDTO.setCommentUser(currentUser.getUserId());
        commentDTO.setCreateTime(now);
        commentDTO.setUpdateTime(now);
        if (commentPoMapper.insertSelective(CommentMS.INSTANCE.toPo(commentDTO)) <= 0) {
            throw BusinessException.build(ResponseCode.OPERATE_FAIL, "新增评论失败");
        }

        return true;
    }

    /**
     * 閸掔娀娅庣拠鍕啈
     *
     * @param commentId
     * @return
     */
    @Override
    public Boolean delete(Integer commentId) {
        List<Integer> commentIds = new ArrayList<>();
        List<CommentDTO> children = new ArrayList<>();
        // 闁俺绻冮悥鍓侀獓ID閼惧嘲褰囩€涙劗楠囩拠鍕啈娣団剝浼?
        this.getAllChildrenByPreId(children, commentId);
        if (CollectionUtils.isNotEmpty(children)) {
            commentIds.addAll(children.stream().map(CommentDTO::getId).collect(Collectors.toList()));
        }
        commentIds.add(commentId);
        CommentPoExample example = new CommentPoExample();
        example.createCriteria().andIdIn(commentIds);

        CommentPo commentPo = new CommentPo();
        commentPo.setIsDeleted(true);
        commentPo.setUpdateTime(LocalDateTime.now());
        if (commentPoMapper.updateByExampleSelective(commentPo, example) <= 0) {
            throw BusinessException.build(ResponseCode.OPERATE_FAIL, "删除评论失败");
        }
        return true;
    }

    /**
     * 闁俺绻冮悥鍓侀獓ID閼惧嘲褰囩€涙劗楠囩拠鍕啈娣団剝浼?
     *
     * @param result 鐎涙ɑ鏂佺紒鎾寸亯
     * @param preId
     * @return
     */
    @Override
    public void getAllChildrenByPreId(List<CommentDTO> result, Integer preId) {
        CommentPoExample example = new CommentPoExample();
        example.createCriteria().andIsDeletedEqualTo(false)
                .andStateEqualTo(true)
                .andPreIdEqualTo(preId);
        List<CommentDTO> commentDTOS = CommentMS.INSTANCE.toDTO(commentPoMapper.selectByExample(example));
        if (CollectionUtils.isNotEmpty(commentDTOS)) {
            result.addAll(commentDTOS);
            commentDTOS.forEach(commentDTO -> {
                this.getAllChildrenByPreId(result, commentDTO.getId());
            });
        }
    }

    @Override
    public Integer getArticleIdByCommentId(Integer commentId) {
        CommentPo commentPo = commentPoMapper.selectByPrimaryKey(commentId);
        return commentPo == null ? null : commentPo.getArticleId();
    }

    @Override
    public CommentDTO getById(Integer commentId) {
        return CommentMS.INSTANCE.toDTO(commentPoMapper.selectByPrimaryKey(commentId));
    }

    /**
     * 閺嬪嫬缂撶拠鍕啈娣団剝浼?
     *
     * @param commentDTOS
     * @param currentUser
     */
    private void buildCommentInfo(List<CommentDTO> commentDTOS, UserSsoDTO currentUser) {
        // 闁俺绻冮悽銊﹀煕id闂嗗棗鎮庨懢宄板絿閻劍鍩涙穱鈩冧紖
        List<Long> userIds = commentDTOS.stream().map(CommentDTO::getCommentUser).collect(Collectors.toList());
        Map<Long, List<UserDTO>> idUsers = userService.getByIds(userIds).stream().collect(Collectors.groupingBy(UserDTO::getId));
        // 閻劋绨崶鐐差槻閺佷即鍣洪幓鎰絿
        Map<Integer, List<CommentDTO>> preIdMap = commentDTOS.stream().collect(Collectors.groupingBy(CommentDTO::getPreId));
        commentDTOS.forEach(commentDTO -> {
            if (idUsers.containsKey(commentDTO.getCommentUser())) {
                commentDTO.setCommentUserName(idUsers.get(commentDTO.getCommentUser()).get(0).getName());
                commentDTO.setPicture(idUsers.get(commentDTO.getCommentUser()).get(0).getPicture());
            }
            // 閼惧嘲褰囬悽銊﹀煕缁涘楠?
            List<UserLevelDTO> userLevelDTOS = userLevelClient.getByUserId(commentDTO.getCommentUser());
            if (CollectionUtils.isNotEmpty(userLevelDTOS)) {
                commentDTO.setLevel(userLevelDTOS.get(0).getLevel());
            }
            // 閼惧嘲褰囬弬鍥╃彿閻愮绂愰弫浼村櫤
            commentDTO.setLikeCount(userLikeCommentClient.getLikeCountCommentId(commentDTO.getId()));
            // 閺勵垰鎯佸鑼病閻愮绂?
            if (currentUser != null) {
                commentDTO.setIsLike(userLikeCommentClient.isLike(commentDTO.getId(), currentUser.getUserId()));
            }
            // 閸ョ偛顦查弫浼村櫤
            if (preIdMap.containsKey(commentDTO.getId())) {
                commentDTO.setRepliesCount(preIdMap.get(commentDTO.getId()).size());
            }
        });
    }

}


