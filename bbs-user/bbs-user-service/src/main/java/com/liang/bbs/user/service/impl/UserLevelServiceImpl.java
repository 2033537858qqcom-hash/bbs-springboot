package com.liang.bbs.user.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.liang.bbs.article.facade.dto.ArticleReadDTO;
import com.liang.bbs.common.enums.UserLevelEnum;
import com.liang.bbs.user.facade.dto.FollowDTO;
import com.liang.bbs.user.facade.dto.UserForumDTO;
import com.liang.bbs.user.facade.dto.UserLevelDTO;
import com.liang.bbs.user.facade.dto.UserSearchDTO;
import com.liang.bbs.user.facade.server.FollowService;
import com.liang.bbs.user.facade.server.LikeService;
import com.liang.bbs.user.facade.server.UserLevelService;
import com.liang.bbs.user.persistence.entity.UserLevelPo;
import com.liang.bbs.user.persistence.entity.UserLevelPoExample;
import com.liang.bbs.user.persistence.mapper.UserLevelPoMapper;
import com.liang.bbs.user.service.client.ArticleArticleClient;
import com.liang.bbs.user.service.client.UserServiceClient;
import com.liang.bbs.user.service.mapstruct.UserLevelMS;
import com.liang.manage.auth.facade.dto.user.UserDTO;
import com.liang.manage.auth.facade.dto.user.UserListDTO;
import com.liang.nansheng.common.auth.UserSsoDTO;
import com.liang.nansheng.common.enums.ResponseCode;
import com.liang.nansheng.common.web.exception.BusinessException;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 */
@Slf4j
@Service
public class UserLevelServiceImpl implements UserLevelService {
    @Autowired
    private UserLevelPoMapper userLevelPoMapper;

    @Autowired
    private LikeService likeService;

    @Autowired
    private FollowService followService;

    @Autowired
    private ArticleArticleClient articleArticleClient;

    @Autowired
    private UserServiceClient userService;

    /**
     * 閸掓稑缂撻悽銊﹀煕缁涘楠囨穱鈩冧紖
     *
     * @param userId
     * @return
     */
    @Override
    public Boolean create(Long userId) {
        List<UserLevelDTO> userLevelDTOS = this.getByUserId(userId);
        if (CollectionUtils.isEmpty(userLevelDTOS)) {
            UserLevelPo userLevelPo = new UserLevelPo();
            userLevelPo.setUserId(userId);
            LocalDateTime now = LocalDateTime.now();
            userLevelPo.setCreateTime(now);
            userLevelPo.setUpdateTime(now);
            if (userLevelPoMapper.insertSelective(userLevelPo) <= 0) {
                throw BusinessException.build(ResponseCode.OPERATE_FAIL, "初始化用户等级信息失败");
            }
        }

        return true;
    }

    /**
     * 閺囧瓨鏌婇悽銊﹀煕缁涘楠囨穱鈩冧紖
     *
     * @param userId 閻劍鍩沬d
     * @param points 缁夘垰鍨?
     * @return
     */
    @Override
    public Boolean update(Long userId, Integer points) {
        UserLevelPoExample example = new UserLevelPoExample();
        example.createCriteria().andUserIdEqualTo(userId);
        List<UserLevelPo> userLevelPos = userLevelPoMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(userLevelPos)) {
            UserLevelPo userLevelPo = userLevelPos.get(0);
            userLevelPo.setUpdateTime(LocalDateTime.now());
            userLevelPo.setPoints(points);
            userLevelPo.setLevel(UserLevelEnum.get(points).name());
            if (userLevelPoMapper.updateByExampleSelective(userLevelPo, example) <= 0) {
                throw BusinessException.build(ResponseCode.OPERATE_FAIL, "更新用户等级信息失败");
            }
        }

        return true;
    }

    /**
     * 閺囧瓨鏌婇幍鈧張澶屾暏閹撮鐡戠痪褌淇婇幁?
     *
     * @return
     */
    @Override
    public Boolean updatePointsAll() {
        // 閹碘偓閺堝娈戦悽銊﹀煕id
        List<Long> userIds = loadAllUserIds();
        if (CollectionUtils.isEmpty(userIds)) {
            return true;
        }
        // 閻劍鍩涢惃鍕瀮缁旂娀妲勭拠缁樻殶闁?
        Map<Long, Long> userIdToArticleReadCount = articleArticleClient.getUserReadCount(userIds).stream()
                .collect(Collectors.toMap(ArticleReadDTO::getUserId, ArticleReadDTO::getArticleReadCount, (v1, v2) -> v1));
        userIds.forEach(userId -> {
            // 閸楁鏁撻崐?= 閺傚洨鐝烽惃鍕鐠囩粯鏆?/ 10 + 閼惧嘲绶遍惃鍕仯鐠х偞鏆?
            long points = (userIdToArticleReadCount.getOrDefault(userId, 0L)) / 10 + likeService.getUserLikeCount(userId);
            this.update(userId, (int) points);
        });

        return true;
    }

    @Override
    public Boolean syncAll() {
        // 閹碘偓閺堝娈戦悽銊﹀煕id
        List<Long> userIds = loadAllUserIds();
        if (CollectionUtils.isEmpty(userIds)) {
            return true;
        }
        // 閸掓稑缂撻悽銊﹀煕缁涘楠囨穱鈩冧紖
        userIds.forEach(this::create);
        return true;
    }

    /**
     * 閼惧嘲褰囬悜顓㈡，娴ｆ粏鈧懎鍨悰?
     *
     * @param userSearchDTO
     * @param currentUser
     * @return
     */
    @Override
    public PageInfo<UserForumDTO> getHotAuthorsList(UserSearchDTO userSearchDTO, UserSsoDTO currentUser) {
        PageHelper.startPage(userSearchDTO.getCurrentPage(), userSearchDTO.getPageSize()).setOrderBy("points desc, user_id desc");
        // 闁俺绻冮悽銊﹀煕id閼惧嘲褰囬悽銊﹀煕缁涘楠囨穱鈩冧紖
        List<UserLevelDTO> userLevelDTOS = getByUserId(null);
        if (CollectionUtils.isEmpty(userLevelDTOS)) {
            return new PageInfo<>(new ArrayList<>());
        }
        // 闁俺绻冮悽銊﹀煕id闂嗗棗鎮庨崢鏄忓箯閸欐牜鏁ら幋铚備繆閹垽绱欐潻娆愮壉閸欘垯浜掓径褍銇囬崙蹇撶毌閺佺増宓佹惔鎾舵畱閹垮秳缍斿▎鈩冩殶閿?
        List<Long> userIds = userLevelDTOS.stream().map(UserLevelDTO::getUserId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(userIds)) {
            return new PageInfo<>(new ArrayList<>());
        }
        // 閻劍鍩涢崺铏诡攨娣団剝浼?
        List<UserDTO> users = loadUsersByIds(userIds);
        if (CollectionUtils.isEmpty(users)) {
            return new PageInfo<>(new ArrayList<>());
        }
        Map<Long, List<UserDTO>> userIdToUsersMap = users.stream().collect(Collectors.groupingBy(UserDTO::getId));
        // 閻劍鍩涢惃鍕瀮缁旂娀妲勭拠缁樻殶闁?
        Map<Long, Long> userIdToArticleReadCount = articleArticleClient.getUserReadCount(userIds).stream()
                .collect(Collectors.toMap(ArticleReadDTO::getUserId, ArticleReadDTO::getArticleReadCount, (v1, v2) -> v1));
        List<UserForumDTO> userForumDTOS = new ArrayList<>();
        userLevelDTOS.forEach(userLevelDTO -> {
            List<UserDTO> matchedUsers = userIdToUsersMap.get(userLevelDTO.getUserId());
            if (CollectionUtils.isEmpty(matchedUsers)) {
                return;
            }
            UserForumDTO userForumDTO = new UserForumDTO();
            BeanUtils.copyProperties(matchedUsers.get(0), userForumDTO);
            userForumDTO.setLikeCount(likeService.getUserLikeCount(userForumDTO.getId()));
            userForumDTO.setReadCount(userIdToArticleReadCount.getOrDefault(userForumDTO.getId(), 0L));
            userForumDTO.setLevel(userLevelDTO.getLevel());
            // 闁俺绻僨romUser閸滃oUser閼惧嘲褰囬崗铏暈娣団剝浼?
            if (currentUser != null) {
                FollowDTO followDTO = followService.getByFromToUser(currentUser.getUserId(), userForumDTO.getId(), false);
                if (followDTO != null) {
                    userForumDTO.setIsFollow(true);
                }
            }

            userForumDTOS.add(userForumDTO);
        });

        return new PageInfo<>(userForumDTOS);
    }

    /**
     * 闁俺绻冮悽銊﹀煕id閼惧嘲褰囬悽銊﹀煕缁涘楠囨穱鈩冧紖
     *
     * @param userId
     * @return
     */
    @Override
    public List<UserLevelDTO> getByUserId(Long userId) {
        UserLevelPoExample example = new UserLevelPoExample();
        UserLevelPoExample.Criteria criteria = example.createCriteria();
        if (userId != null) {
            criteria.andUserIdEqualTo(userId);
        }
        return UserLevelMS.INSTANCE.toDTO(userLevelPoMapper.selectByExample(example));
    }

    /**
     * 闁俺绻冮悽銊﹀煕id闂嗗棗鎮庨懢宄板絿閻劍鍩涚粵澶岄獓娣団剝浼?
     *
     * @param userIds
     * @return
     */
    @Override
    public List<UserLevelDTO> getByUserIds(List<Long> userIds) {
        UserLevelPoExample example = new UserLevelPoExample();
        example.createCriteria().andUserIdIn(userIds);
        return UserLevelMS.INSTANCE.toDTO(userLevelPoMapper.selectByExample(example));
    }

    /**
     * 閼惧嘲褰囬悽銊﹀煕娣団剝浼?
     *
     * @param userId
     * @param currentUser
     * @return
     */
    @Override
    public UserForumDTO getUserInfo(Long userId, UserSsoDTO currentUser) {
        UserForumDTO userForumDTO = new UserForumDTO();
        // 闁俺绻冮悽銊﹀煕id閼惧嘲褰囬悽銊﹀煕缁涘楠囨穱鈩冧紖
        List<UserLevelDTO> userLevelDTOS = getByUserId(userId);
        if (CollectionUtils.isEmpty(userLevelDTOS)) {
            throw BusinessException.build(ResponseCode.NOT_EXISTS, "用户等级信息不存在");
        }

        List<UserDTO> users = loadUsersByIds(Collections.singletonList(userId));
        if (CollectionUtils.isEmpty(users)) {
            throw BusinessException.build(ResponseCode.NOT_EXISTS, "用户信息不存在或认证服务未启动");
        }
        UserDTO userDTO = users.get(0);
        BeanUtils.copyProperties(userDTO, userForumDTO);
        userForumDTO.setLikeCount(likeService.getUserLikeCount(userDTO.getId()));
        List<ArticleReadDTO> articleReadDTOS = articleArticleClient.getUserReadCount(Collections.singletonList(userDTO.getId()));
        userForumDTO.setReadCount(CollectionUtils.isEmpty(articleReadDTOS) ? 0L : articleReadDTOS.get(0).getArticleReadCount());
        userForumDTO.setLevel(userLevelDTOS.get(0).getLevel());
        userForumDTO.setPoints(userLevelDTOS.get(0).getPoints());
        // 闁俺绻僨romUser閸滃oUser閼惧嘲褰囬崗铏暈娣団剝浼?
        if (currentUser != null) {
            FollowDTO followDTO = followService.getByFromToUser(currentUser.getUserId(), userDTO.getId(), false);
            if (followDTO != null) {
                userForumDTO.setIsFollow(true);
            }
        }

        return userForumDTO;
    }

    private List<Long> loadAllUserIds() {
        try {
            return userService.getAllList().stream().map(UserListDTO::getId).collect(Collectors.toList());
        } catch (FeignException.ServiceUnavailable e) {
            log.debug("本地认证服务未启动或不可用,跳过加载用户ID列表");
            return Collections.emptyList();
        } catch (FeignException.NotFound e) {
            log.warn("接口路径错误,请检查 UserServiceClient 的 path 配置是否正确");
            return Collections.emptyList();
        } catch (Exception e) {
            log.warn("加载用户ID列表失败", e);
            return Collections.emptyList();
        }
    }

    private List<UserDTO> loadUsersByIds(List<Long> userIds) {
        if (CollectionUtils.isEmpty(userIds)) {
            return Collections.emptyList();
        }
        try {
            return userService.getByIds(userIds);
        } catch (FeignException.ServiceUnavailable e) {
            log.debug("本地认证服务未启动或不可用,跳过加载用户详情. userCount={}", userIds.size());
            return Collections.emptyList();
        } catch (Exception e) {
            log.warn("加载用户详情失败. userCount={}", userIds.size(), e);
            return Collections.emptyList();
        }
    }

}


