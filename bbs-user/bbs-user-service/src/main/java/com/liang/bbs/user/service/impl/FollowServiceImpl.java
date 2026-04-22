package com.liang.bbs.user.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.liang.bbs.article.facade.dto.ArticleReadDTO;
import com.liang.bbs.user.facade.dto.FollowCountDTO;
import com.liang.bbs.user.facade.dto.FollowDTO;
import com.liang.bbs.user.facade.dto.FollowSearchDTO;
import com.liang.bbs.user.facade.server.FollowService;
import com.liang.bbs.user.facade.server.LikeService;
import com.liang.bbs.user.facade.server.UserLevelService;
import com.liang.bbs.user.persistence.entity.FollowPo;
import com.liang.bbs.user.persistence.entity.FollowPoExample;
import com.liang.bbs.user.persistence.mapper.FollowPoMapper;
import com.liang.bbs.user.service.client.ArticleArticleClient;
import com.liang.bbs.user.service.client.UserServiceClient;
import com.liang.bbs.user.service.mapstruct.FollowMS;
import com.liang.manage.auth.facade.dto.user.UserDTO;
import com.liang.nansheng.common.auth.UserSsoDTO;
import com.liang.nansheng.common.enums.ResponseCode;
import com.liang.nansheng.common.web.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

/**
 */
@Slf4j
@Service
public class FollowServiceImpl implements FollowService {
    @Autowired
    private FollowPoMapper followPoMapper;

    @Autowired
    private UserServiceClient userService;

    @Autowired
    private ArticleArticleClient articleArticleClient;

    @Autowired
    private UserLevelService userlevelService;

    @Autowired
    private LikeService likeService;

    /**
     * 閼惧嘲褰囬幍鈧張澶屾畱閸忚櫕鏁?
     *
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public List<FollowDTO> getPaasAll(LocalDateTime startTime, LocalDateTime endTime) {
        FollowPoExample example = new FollowPoExample();
        example.createCriteria().andStateEqualTo(true)
                .andCreateTimeBetween(startTime, endTime);
        return FollowMS.INSTANCE.toDTO(followPoMapper.selectByExample(example));
    }

    /**
     * 閼惧嘲褰囬崗铏暈閻ㄥ嫮鏁ら幋铚備繆閹?
     *
     * @param followSearchDTO
     * @param currentUser
     * @return
     */
    @Override
    public PageInfo<FollowDTO> getFollowUsers(FollowSearchDTO followSearchDTO, UserSsoDTO currentUser) {
        // 閼惧嘲褰囨径褏澧伴敍鍫㈢焽娑撴繄娈慽d閿?
        Long getBigCow = followSearchDTO.getGetBigCow();
        // 閼惧嘲褰囩划澶夌閿涘牆銇囬悧娑氭畱id閿?
        Long getFan = followSearchDTO.getGetFan();
        if (getBigCow == null && getFan == null) {
            throw BusinessException.build(ResponseCode.NOT_EXISTS, "缺少查询用户参数");
        }
        if (getBigCow != null && getFan != null) {
            throw BusinessException.build(ResponseCode.NOT_EXISTS, "查询参数不能同时存在");
        }
        FollowPoExample example = new FollowPoExample();
        FollowPoExample.Criteria criteria = example.createCriteria().andStateEqualTo(true);
        if (getBigCow != null) {
            // 閼惧嘲褰囨径褏澧版穱鈩冧紖
            criteria.andFromUserEqualTo(getBigCow);
        }
        if (getFan != null) {
            // 閼惧嘲褰囩划澶夌娣団剝浼?
            criteria.andToUserEqualTo(getFan);
        }
        example.setOrderByClause("`id` desc");

        PageHelper.startPage(followSearchDTO.getCurrentPage(), followSearchDTO.getPageSize());
        List<FollowPo> followPos = followPoMapper.selectByExample(example);
        PageInfo<FollowDTO> pageInfo = FollowMS.INSTANCE.toPage(new PageInfo<>(followPos));
        pageInfo.getList().forEach(followDTO -> {
            UserDTO userDTO = new UserDTO();
            if (getBigCow != null) {
                userDTO = userService.getById(followDTO.getToUser());
            }
            if (getFan != null) {
                userDTO = userService.getById(followDTO.getFromUser());
            }
            followDTO.setName(userDTO.getName());
            followDTO.setPicture(userDTO.getPicture());
            followDTO.setIntro(userDTO.getIntro());
            followDTO.setLevel(userlevelService.getByUserId(userDTO.getId()).get(0).getLevel());
            followDTO.setLikeCount(likeService.getUserLikeCount(userDTO.getId()));
            List<ArticleReadDTO> articleReadDTOS = articleArticleClient.getUserReadCount(Collections.singletonList(userDTO.getId()));
            followDTO.setReadCount(CollectionUtils.isEmpty(articleReadDTOS) ? 0L : articleReadDTOS.get(0).getArticleReadCount());
            // 闁俺绻僨romUser閸滃oUser閼惧嘲褰囬崗铏暈娣団剝浼?
            if (currentUser != null) {
                FollowDTO followed = getByFromToUser(currentUser.getUserId(), userDTO.getId(), false);
                if (followed != null) {
                    followDTO.setIsFollow(true);
                }
            }
        });

        return pageInfo;
    }

    /**
     * 闁俺绻僫d閹存牞鈧懎鍙у▔銊や繆閹?
     *
     * @param id
     * @return
     */
    @Override
    public FollowDTO getById(Integer id) {
        return FollowMS.INSTANCE.toDTO(followPoMapper.selectByPrimaryKey(id));
    }

    /**
     * 闁俺绻僨romUser閸滃oUser閼惧嘲褰囬崗铏暈娣団剝浼?
     *
     * @param fromUser
     * @param toUser
     * @param isAll true:娑撳秴灏崚鍡楀彠濞夈劋绗岄崥锔肩礉false:閸欘亝鐓＄拠銏犲彠濞夈劋绨￠惃?
     * @return
     */
    @Override
    public FollowDTO getByFromToUser(Long fromUser, Long toUser, Boolean isAll) {
        FollowPoExample example = new FollowPoExample();
        FollowPoExample.Criteria criteria = example.createCriteria().andFromUserEqualTo(fromUser)
                .andToUserEqualTo(toUser);
        if (!isAll) {
            criteria.andStateEqualTo(true);
        }

        List<FollowPo> followPos = followPoMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(followPos)) {
            return null;
        }

        return FollowMS.INSTANCE.toDTO(followPos.get(0));
    }

    /**
     * 閺囧瓨鏌婇崗铏暈閻樿埖鈧?
     *
     * @param fromUser
     * @param toUser
     * @return
     */
    @Override
    public Boolean updateFollowState(Long fromUser, Long toUser) {
        if (fromUser.equals(toUser)) {
            throw BusinessException.build(ResponseCode.DATA_ILLEGAL, "不能关注自己");
        }
        FollowDTO followDTO = getByFromToUser(fromUser, toUser, true);
        LocalDateTime now = LocalDateTime.now();
        // 濞屸剝婀侀敍灞炬煀婢?
        if (followDTO == null) {
            FollowPo followPo = new FollowPo();
            followPo.setFromUser(fromUser);
            followPo.setToUser(toUser);
            followPo.setState(true);
            followPo.setCreateTime(now);
            followPo.setUpdateTime(now);
            if (followPoMapper.insertSelective(followPo) <= 0) {
                throw BusinessException.build(ResponseCode.OPERATE_FAIL, "新增关注关系失败");
            }
        } else {
            FollowPo followPo = new FollowPo();
            followPo.setId(followDTO.getId());
            // 閻樿埖鈧礁褰囬崣?
            followPo.setState(!followDTO.getState());
            followPo.setUpdateTime(now);
            if (followPoMapper.updateByPrimaryKeySelective(followPo) <= 0) {
                throw BusinessException.build(ResponseCode.OPERATE_FAIL, "更新关注状态失败");
            }
        }

        return true;
    }

    /**
     * 閼惧嘲褰囬崗铏暈/缁绗ｉ弫浼村櫤
     *
     * @param userId
     * @return
     */
    @Override
    public FollowCountDTO getFollowCount(Long userId) {
        // 閸忚櫕鏁為弫浼村櫤
        FollowCountDTO followCountDTO = new FollowCountDTO();
        FollowPoExample example = new FollowPoExample();
        example.createCriteria().andStateEqualTo(true)
                .andFromUserEqualTo(userId);
        followCountDTO.setFollowCount(followPoMapper.countByExample(example));

        // 缁绗ｉ弫浼村櫤
        FollowPoExample example2 = new FollowPoExample();
        example2.createCriteria().andStateEqualTo(true)
                .andToUserEqualTo(userId);
        followCountDTO.setFanCount(followPoMapper.countByExample(example2));

        return followCountDTO;
    }
}


