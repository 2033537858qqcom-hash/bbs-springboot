package com.liang.bbs.article.service.impl;

import com.liang.bbs.article.facade.dto.ArticleLabelDTO;
import com.liang.bbs.article.facade.server.ArticleLabelService;
import com.liang.bbs.article.persistence.entity.ArticleLabelPo;
import com.liang.bbs.article.persistence.entity.ArticleLabelPoExample;
import com.liang.bbs.article.persistence.mapper.ArticleLabelPoExMapper;
import com.liang.bbs.article.persistence.mapper.ArticleLabelPoMapper;
import com.liang.bbs.article.service.mapstruct.ArticleLabelMS;
import com.liang.nansheng.common.auth.UserSsoDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 */
@Slf4j
@Service
public class ArticleLableServiceImpl implements ArticleLabelService {
    @Autowired
    private ArticleLabelPoMapper articleLabelPoMapper;

    @Autowired
    private ArticleLabelPoExMapper articleLabelPoExMapper;

    /**
     * 閺傛澘顤冮弬鍥︽閺嶅洨顒烽崗宕囬兇娣団剝浼?
     *
     * @param labelIds
     * @param articleId
     * @param currentUser
     * @return
     */
    @Override
    public Boolean create(List<Integer> labelIds, Integer articleId, UserSsoDTO currentUser) {
        LocalDateTime now = LocalDateTime.now();
        labelIds.forEach(labelId -> {
            ArticleLabelPo articleLabelPo = new ArticleLabelPo();
            articleLabelPo.setArticleId(articleId);
            articleLabelPo.setLabelId(labelId);
            articleLabelPo.setCreateUser(currentUser.getUserId());
            articleLabelPo.setUpdateUser(currentUser.getUserId());
            articleLabelPo.setCreateTime(now);
            articleLabelPo.setUpdateTime(now);
            articleLabelPo.setIsDeleted(false);
            articleLabelPoMapper.insertSelective(articleLabelPo);
        });
        return true;
    }

    /**
     * 閺囧瓨鏌婇弬鍥︽閺嶅洨顒烽崗宕囬兇娣団剝浼?
     *
     * @param labelIds
     * @param articleId
     * @param currentUser
     * @return
     */
    @Override
    public Boolean update(List<Integer> labelIds, Integer articleId, UserSsoDTO currentUser) {
        // 閺嶈宓侀弬鍥╃彿id闂嗗棗鎮庨懢宄板絿閺傚洨鐝烽弽鍥╊劮娣団剝浼?
        List<ArticleLabelDTO> articleLabelDTOS = getByArticleIds(Collections.singletonList(articleId));
        List<Integer> labelIdsOld = articleLabelDTOS.stream().distinct().map(ArticleLabelDTO::getLabelId).collect(Collectors.toList());

        // 闂団偓鐟曚焦鏌婃晶鐐垫畱
        List<Integer> labelIdsCreate = new ArrayList<>();
        labelIds.forEach(labelId -> {
            if (!labelIdsOld.contains(labelId)) {
                labelIdsCreate.add(labelId);
            }
        });
        create(labelIdsCreate, articleId, currentUser);

        // 闂団偓鐟曚礁鍨归梽銈囨畱
        labelIdsOld.forEach(labelId -> {
            if (!labelIds.contains(labelId)) {
                ArticleLabelPoExample example = new ArticleLabelPoExample();
                example.createCriteria().andArticleIdEqualTo(articleId)
                        .andLabelIdEqualTo(labelId)
                        .andIsDeletedEqualTo(false);

                ArticleLabelPo articleLabelPo = new ArticleLabelPo();
                articleLabelPo.setUpdateUser(currentUser.getUserId());
                articleLabelPo.setUpdateTime(LocalDateTime.now());
                articleLabelPo.setIsDeleted(true);
                articleLabelPoMapper.updateByExampleSelective(articleLabelPo, example);
            }
        });

        return true;
    }

    /**
     * 閺嶈宓侀弽鍥╊劮id閼惧嘲褰囬弬鍥╃彿閺嶅洨顒锋穱鈩冧紖
     *
     * @param labelIds
     * @return
     */
    @Override
    public List<ArticleLabelDTO> getByLabelIds(List<Integer> labelIds) {
        ArticleLabelPoExample example = new ArticleLabelPoExample();
        example.createCriteria().andIsDeletedEqualTo(false)
                .andLabelIdIn(labelIds);

        return ArticleLabelMS.INSTANCE.toDTO(articleLabelPoMapper.selectByExample(example));
    }

    /**
     * 閺嶈宓侀弬鍥╃彿id闂嗗棗鎮庨懢宄板絿閺傚洨鐝烽弽鍥╊劮娣団剝浼?
     *
     * @param articleIds
     * @return
     */
    @Override
    public List<ArticleLabelDTO> getByArticleIds(List<Integer> articleIds) {
        ArticleLabelPoExample example = new ArticleLabelPoExample();
        example.createCriteria().andIsDeletedEqualTo(false)
                .andArticleIdIn(articleIds);

        return ArticleLabelMS.INSTANCE.toDTO(articleLabelPoMapper.selectByExample(example));
    }

    /**
     * 閼惧嘲褰囬弽鍥╊劮娴ｈ法鏁ら弫浼村櫤
     *
     * @param labelId
     * @return
     */
    @Override
    public Long getCountByLabelId(Integer labelId) {
        return articleLabelPoExMapper.countByLabelId(labelId);
    }

}


