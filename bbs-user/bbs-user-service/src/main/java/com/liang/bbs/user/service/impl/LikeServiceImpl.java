package com.liang.bbs.user.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.liang.bbs.article.facade.dto.ArticleDTO;
import com.liang.bbs.article.facade.dto.InternalBaseArticleIdsRequest;
import com.liang.bbs.common.enums.ArticleStateEnum;
import com.liang.bbs.user.facade.dto.LikeDTO;
import com.liang.bbs.user.facade.dto.LikeSearchDTO;
import com.liang.bbs.user.facade.server.LikeService;
import com.liang.bbs.user.persistence.entity.LikePo;
import com.liang.bbs.user.persistence.entity.LikePoExample;
import com.liang.bbs.user.persistence.mapper.LikePoExMapper;
import com.liang.bbs.user.persistence.mapper.LikePoMapper;
import com.liang.bbs.user.service.client.ArticleArticleClient;
import com.liang.bbs.user.service.mapstruct.LikeMS;
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
import java.util.List;
import java.util.stream.Collectors;

/**
 */
@Slf4j
@Service
public class LikeServiceImpl implements LikeService {
    @Autowired
    private LikePoMapper likePoMapper;

    @Autowired
    private LikePoExMapper likePoExMapper;

    @Autowired
    private ArticleArticleClient articleArticleClient;

    /**
     * 閼惧嘲褰囬幍鈧張澶屽仯鐠х偟娈戦柅姘崇箖鐎光剝鐗抽惃鍕瀮缁旂姳淇婇幁?
     *
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public List<LikeDTO> getPaasAll(LocalDateTime startTime, LocalDateTime endTime) {
        return LikeMS.INSTANCE.toDTO(likePoExMapper.selectAllArticle(startTime, endTime));
    }

    /**
     * 闁俺绻冮悽銊﹀煕id閼惧嘲褰囬悙纭呯閻ㄥ嫭鏋冪粩鐘变繆閹?
     *
     * @param likeSearchDTO
     * @return
     */
    @Override
    public PageInfo<LikeDTO> getArticleByUserId(LikeSearchDTO likeSearchDTO) {
        if (likeSearchDTO.getLikeUser() == null) {
            throw BusinessException.build(ResponseCode.NOT_EXISTS, "缺少点赞用户参数");
        }

        PageHelper.startPage(likeSearchDTO.getCurrentPage(), likeSearchDTO.getPageSize());
        List<LikePo> likePos = likePoExMapper.selectArticleByUserId(likeSearchDTO.getLikeUser());

        return LikeMS.INSTANCE.toPage(new PageInfo<>(likePos));
    }

    /**
     * 闁俺绻冮弬鍥╃彿id閼惧嘲褰囬悙纭呯閻ㄥ嫮鏁ら幋铚備繆閹?
     *
     * @param likeSearchDTO
     * @return
     */
    @Override
    public PageInfo<LikeDTO> getUserByArticleId(LikeSearchDTO likeSearchDTO) {
        if (likeSearchDTO.getArticleId() == null) {
            throw BusinessException.build(ResponseCode.NOT_EXISTS, "缺少文章参数");
        }
        LikePoExample example = new LikePoExample();
        LikePoExample.Criteria criteria = example.createCriteria().andStateEqualTo(true);
        criteria.andArticleIdEqualTo(likeSearchDTO.getArticleId());
        example.setOrderByClause("`id` desc");

        PageHelper.startPage(likeSearchDTO.getCurrentPage(), likeSearchDTO.getPageSize());
        List<LikePo> likePos = likePoMapper.selectByExample(example);

        return LikeMS.INSTANCE.toPage(new PageInfo<>(likePos));
    }

    /**
     * 闁俺绻僫d閼惧嘲褰囬悙纭呯娣団剝浼?
     *
     * @param id
     * @return
     */
    @Override
    public LikeDTO getById(Integer id) {
        return LikeMS.INSTANCE.toDTO(likePoMapper.selectByPrimaryKey(id));
    }

    /**
     * 闁俺绻冮弬鍥╃彿id閸滃瞼鏁ら幋绌抎閼惧嘲褰囬悙纭呯娣団剝浼?
     *
     * @param articleId
     * @param userId
     * @return
     */
    @Override
    public LikeDTO getByArticleIdUserId(Integer articleId, Long userId) {
        LikePoExample example = new LikePoExample();
        example.createCriteria().andArticleIdEqualTo(articleId).andLikeUserEqualTo(userId);
        List<LikeDTO> likeDTOS = LikeMS.INSTANCE.toDTO(likePoMapper.selectByExample(example));
        if (CollectionUtils.isEmpty(likeDTOS)) {
            return null;
        }
        return likeDTOS.get(0);
    }

    /**
     * 閼惧嘲褰囬弬鍥╃彿閻ㄥ嫮鍋ｇ挧鐐存殶闁?
     *
     * @param articleIds
     * @return
     */
    @Override
    public Long getLikeCountArticle(List<Integer> articleIds) {
        LikePoExample example = new LikePoExample();
        example.createCriteria().andStateEqualTo(true)
                .andArticleIdIn(articleIds);
        return likePoMapper.countByExample(example);
    }

    /**
     * 閺勵垰鎯侀悙纭呯
     *
     * @param articleId
     * @param userId
     * @return
     */
    @Override
    public Boolean isLike(Integer articleId, Long userId) {
        LikePoExample example = new LikePoExample();
        example.createCriteria().andStateEqualTo(true)
                .andArticleIdEqualTo(articleId)
                .andLikeUserEqualTo(userId);
        return likePoMapper.countByExample(example) > 0;
    }

    /**
     * 閺囧瓨鏌婇悙纭呯閻樿埖鈧?
     *
     * @param articleId
     * @param currentUser
     * @return
     */
    @Override
    public Boolean updateLikeState(Integer articleId, UserSsoDTO currentUser) {
        LikeDTO likeDTO = getByArticleIdUserId(articleId, currentUser.getUserId());
        LocalDateTime now = LocalDateTime.now();
        // 濞屸剝婀侀敍灞炬煀婢?
        if (likeDTO == null) {
            LikePo likePo = new LikePo();
            likePo.setArticleId(articleId);
            likePo.setState(true);
            likePo.setLikeUser(currentUser.getUserId());
            likePo.setCreateTime(now);
            likePo.setUpdateTime(now);
            if (likePoMapper.insertSelective(likePo) <= 0) {
                throw BusinessException.build(ResponseCode.OPERATE_FAIL, "新增点赞失败");
            }
        } else {
            // 閻樿埖鈧礁褰囬崣?
            likeDTO.setState(!likeDTO.getState());
            likeDTO.setUpdateTime(now);
            if (likePoMapper.updateByPrimaryKeySelective(LikeMS.INSTANCE.toPo(likeDTO)) <= 0) {
                throw BusinessException.build(ResponseCode.OPERATE_FAIL, "更新点赞状态失败");
            }
        }

        return true;
    }

    /**
     * 閻劍鍩涢懢宄板絿閻ㄥ嫮鍋ｇ挧鐐存殶闁?
     *
     * @param userId
     * @return
     */
    @Override
    public Long getUserLikeCount(Long userId) {
        List<ArticleDTO> articleDTOS = articleArticleClient.getByUserId(userId);
        if (CollectionUtils.isNotEmpty(articleDTOS)) {
            List<Integer> articleIds = articleDTOS.stream().map(ArticleDTO::getId).collect(Collectors.toList());
            return this.getLikeCountArticle(articleIds);
        }

        return 0L;
    }

    @Override
    public Long getUserTheLikeCount(Long userId) {
        LikePoExample example = new LikePoExample();
        example.createCriteria().andStateEqualTo(true)
                .andLikeUserEqualTo(userId);
        List<LikePo> likePos = likePoMapper.selectByExample(example);

        if (CollectionUtils.isNotEmpty(likePos)) {
            List<Integer> articleIds = likePos.stream().map(LikePo::getArticleId).collect(Collectors.toList());
            InternalBaseArticleIdsRequest request = new InternalBaseArticleIdsRequest();
            request.setIds(articleIds);
            request.setArticleStateEnum(ArticleStateEnum.enable);
            List<ArticleDTO> articleDTOS = articleArticleClient.getBaseByIds(request);
            return CollectionUtils.isNotEmpty(articleDTOS) ? articleDTOS.size() : 0L;
        }

        return 0L;
    }
}


