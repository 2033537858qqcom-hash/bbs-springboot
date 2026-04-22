package com.liang.bbs.user.service.impl;

import com.liang.bbs.user.facade.dto.LikeCommentDTO;
import com.liang.bbs.user.facade.server.LikeCommentService;
import com.liang.bbs.user.persistence.entity.LikeCommentPo;
import com.liang.bbs.user.persistence.entity.LikeCommentPoExample;
import com.liang.bbs.user.persistence.mapper.LikeCommentPoExMapper;
import com.liang.bbs.user.persistence.mapper.LikeCommentPoMapper;
import com.liang.bbs.user.service.mapstruct.LikeCommentMS;
import com.liang.nansheng.common.auth.UserSsoDTO;
import com.liang.nansheng.common.enums.ResponseCode;
import com.liang.nansheng.common.web.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 */
@Slf4j
@Service
public class LikeCommentServiceImpl implements LikeCommentService {
    @Autowired
    private LikeCommentPoMapper likeCommentPoMapper;

    @Autowired
    private LikeCommentPoExMapper likeCommentPoExMapper;

    @Override
    public List<LikeCommentDTO> getPaasAll(LocalDateTime startTime, LocalDateTime endTime) {
        return LikeCommentMS.INSTANCE.toDTO(likeCommentPoExMapper.selectAllCommentLike(startTime, endTime));
    }

    /**
     * 閼惧嘲褰囩拠鍕啈閻ㄥ嫮鍋ｇ挧鐐存殶闁?
     *
     * @param commentId
     * @return
     */
    @Override
    public Long getLikeCountCommentId(Integer commentId) {
        LikeCommentPoExample example = new LikeCommentPoExample();
        example.createCriteria().andStateEqualTo(true)
                .andCommentIdEqualTo(commentId);
        return likeCommentPoMapper.countByExample(example);
    }

    /**
     * 閺勵垰鎯侀悙纭呯
     *
     * @param commentId
     * @param userId
     * @return
     */
    @Override
    public Boolean isLike(Integer commentId, Long userId) {
        LikeCommentPoExample example = new LikeCommentPoExample();
        example.createCriteria().andStateEqualTo(true)
                .andCommentIdEqualTo(commentId)
                .andLikeUserEqualTo(userId);
        return likeCommentPoMapper.countByExample(example) > 0;
    }

    /**
     * 閺囧瓨鏌婇悙纭呯閻樿埖鈧?
     *
     * @param commentId
     * @param currentUser
     * @return
     */
    @Override
    public Boolean updateLikeCommentState(Integer commentId, UserSsoDTO currentUser) {
        LikeCommentDTO commentDTO = getByCommentIdUserId(commentId, currentUser.getUserId());
        LocalDateTime now = LocalDateTime.now();
        // 濞屸剝婀侀敍灞炬煀婢?
        if (commentDTO == null) {
            LikeCommentPo likeCommentPo = new LikeCommentPo();
            likeCommentPo.setCommentId(commentId);
            likeCommentPo.setState(true);
            likeCommentPo.setLikeUser(currentUser.getUserId());
            likeCommentPo.setCreateTime(now);
            likeCommentPo.setUpdateTime(now);
            if (likeCommentPoMapper.insertSelective(likeCommentPo) <= 0) {
                throw BusinessException.build(ResponseCode.OPERATE_FAIL, "濞ｈ濮炵拠鍕啈閻愮绂愭径杈Е");
            }
        } else {
            // 閻樿埖鈧礁褰囬崣?
            commentDTO.setState(!commentDTO.getState());
            commentDTO.setUpdateTime(now);
            if (likeCommentPoMapper.updateByPrimaryKeySelective(LikeCommentMS.INSTANCE.toPo(commentDTO)) <= 0) {
                throw BusinessException.build(ResponseCode.OPERATE_FAIL, "更新评论点赞状态失败");
            }
        }

        return true;
    }

    /**
     * 闁俺绻冪拠鍕啈id閸滃瞼鏁ら幋绌抎閼惧嘲褰囬悙纭呯娣団剝浼?
     *
     * @param commentId
     * @param userId
     * @return
     */
    @Override
    public LikeCommentDTO getByCommentIdUserId(Integer commentId, Long userId) {
        LikeCommentPoExample example = new LikeCommentPoExample();
        example.createCriteria().andCommentIdEqualTo(commentId).andLikeUserEqualTo(userId);
        List<LikeCommentDTO> likeCommentDTOS = LikeCommentMS.INSTANCE.toDTO(likeCommentPoMapper.selectByExample(example));
        if (CollectionUtils.isEmpty(likeCommentDTOS)) {
            return null;
        }
        return likeCommentDTOS.get(0);
    }

}


