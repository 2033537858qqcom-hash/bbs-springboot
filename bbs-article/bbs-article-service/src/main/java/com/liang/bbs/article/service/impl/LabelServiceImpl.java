package com.liang.bbs.article.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.liang.bbs.article.facade.dto.LabelDTO;
import com.liang.bbs.article.facade.dto.LabelSearchDTO;
import com.liang.bbs.article.facade.server.ArticleLabelService;
import com.liang.bbs.article.facade.server.LabelService;
import com.liang.bbs.article.persistence.entity.LabelPo;
import com.liang.bbs.article.persistence.entity.LabelPoExample;
import com.liang.bbs.article.persistence.mapper.LabelPoMapper;
import com.liang.bbs.article.service.client.FileServiceClient;
import com.liang.bbs.article.service.mapstruct.LabelMS;
import com.liang.nansheng.common.auth.UserSsoDTO;
import com.liang.nansheng.common.enums.ImageTypeEnum;
import com.liang.nansheng.common.enums.ResponseCode;
import com.liang.nansheng.common.web.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 */
@Slf4j
@Service
public class LabelServiceImpl implements LabelService {
    @Autowired
    private LabelPoMapper labelPoMapper;

    @Autowired
    private ArticleLabelService articleLabelService;

    @Autowired
    private FileServiceClient fileService;

    /**
     * 閼惧嘲褰囬弽鍥╊劮
     *
     * @param labelSearchDTO
     * @return
     */
    @Override
    public PageInfo<LabelDTO> getList(LabelSearchDTO labelSearchDTO) {
        LabelPoExample example = new LabelPoExample();
        LabelPoExample.Criteria criteria = example.createCriteria().andIsDeletedEqualTo(false);
        if (labelSearchDTO.getId() != null) {
            criteria.andIdEqualTo(labelSearchDTO.getId());
        }
        if (StringUtils.isNotBlank(labelSearchDTO.getLabelName())) {
            criteria.andLabelNameLike("%" + labelSearchDTO.getLabelName() + "%");
        }
        example.setOrderByClause("`id` desc");

        PageHelper.startPage(labelSearchDTO.getCurrentPage(), labelSearchDTO.getPageSize());
        PageInfo<LabelDTO> pageInfo = LabelMS.INSTANCE.toPage(new PageInfo<>(labelPoMapper.selectByExample(example)));
        pageInfo.getList().forEach(labelDTO -> {
            labelDTO.setArticleUseCount(articleLabelService.getCountByLabelId(labelDTO.getId()));
        });

        return pageInfo;
    }

    /**
     * 闁俺绻冮弽鍥╊劮id闂嗗棗鎮庨懢宄板絿閺嶅洨顒锋穱鈩冧紖
     *
     * @param ids
     * @return
     */
    @Override
    public List<LabelDTO> getByIds(List<Integer> ids) {
        LabelPoExample example = new LabelPoExample();
        example.createCriteria().andIsDeletedEqualTo(false).andIdIn(ids);
        return LabelMS.INSTANCE.toDTO(labelPoMapper.selectByExample(example));
    }

    /**
     * 閺傛澘顤冮弽鍥╊劮
     *
     * @param labelDTO
     * @param currentUser
     * @return
     */
    @Override
    public Boolean create(LabelDTO labelDTO, UserSsoDTO currentUser) {
        if (StringUtils.isBlank(labelDTO.getLabelName())) {
            throw BusinessException.build(ResponseCode.NOT_EXISTS, "标签名称不能为空");
        }
        if (isNameExist(null, labelDTO.getLabelName())) {
            throw BusinessException.build(ResponseCode.NAME_EXIST, "标签名称已存在");
        }

        labelDTO.setIsDeleted(false);
        labelDTO.setCreateUser(currentUser.getUserId());
        labelDTO.setUpdateUser(currentUser.getUserId());
        LocalDateTime now = LocalDateTime.now();
        labelDTO.setCreateTime(now);
        labelDTO.setUpdateTime(now);
        LabelPo labelPo = LabelMS.INSTANCE.toPo(labelDTO);
        if (labelPoMapper.insertSelective(labelPo) <= 0) {
            throw BusinessException.build(ResponseCode.OPERATE_FAIL, "新增标签失败");
        }

        return true;
    }


    /**
     * 娑撳﹣绱堕弽鍥╊劮logo
     *
     * @param bytes
     * @param sourceFileName
     * @return
     */
    @Override
    public String uploadLabelLogo(byte[] bytes, String sourceFileName) {
        try {
            // 閺傚洣娆㈡稉濠佺炊閿涘牆澹€閸掑浄绱?
            return fileService.fileCutUpload(bytes, sourceFileName, ImageTypeEnum.labelPicture.name());
        } catch (Exception e) {
            log.error("上传标签图标失败", e);
            throw BusinessException.build(ResponseCode.OPERATE_FAIL, "上传标签图标失败");
        }
    }

    /**
     * 閺囧瓨鏌婇弽鍥╊劮
     *
     * @param labelDTO
     * @param currentUser
     * @return
     */
    @Override
    public Boolean update(LabelDTO labelDTO, UserSsoDTO currentUser) {
        if (StringUtils.isBlank(labelDTO.getLabelName())) {
            throw BusinessException.build(ResponseCode.NOT_EXISTS, "标签名称不能为空");
        }
        if (isNameExist(labelDTO.getId(), labelDTO.getLabelName())) {
            throw BusinessException.build(ResponseCode.NAME_EXIST, "标签名称已存在");
        }
        labelDTO.setIsDeleted(null);
        labelDTO.setCreateUser(null);
        labelDTO.setUpdateUser(currentUser.getUserId());
        labelDTO.setCreateTime(null);
        labelDTO.setUpdateTime(LocalDateTime.now());
        LabelPo labelPo = LabelMS.INSTANCE.toPo(labelDTO);
        if (labelPoMapper.updateByPrimaryKeySelective(labelPo) <= 0) {
            throw BusinessException.build(ResponseCode.OPERATE_FAIL, "更新标签失败");
        }

        return true;
    }

    /**
     * 閸掔娀娅庨弽鍥╊劮
     *
     * @param id
     * @return
     */
    @Override
    public Boolean delete(Integer id) {
        LabelPo labelPo = new LabelPo();
        labelPo.setId(id);
        labelPo.setIsDeleted(true);
        if (labelPoMapper.updateByPrimaryKeySelective(labelPo) <= 0) {
            throw BusinessException.build(ResponseCode.OPERATE_FAIL, "閸掔娀娅庢径杈Е");
        }

        return true;
    }

    /**
     * 閸掋倖鏌囬弽鍥╊劮閸氬秶袨閺勵垰鎯佸鑼病鐎涙ê婀?
     *
     * @param labelId   閺嶅洨顒穒d
     * @param labelName 閺嶅洨顒烽崥宥囆?
     * @return
     */
    private boolean isNameExist(Integer labelId, String labelName) {
        LabelPoExample example = new LabelPoExample();
        example.createCriteria().andIsDeletedEqualTo(false)
                .andLabelNameEqualTo(labelName);
        List<LabelPo> labelPos = labelPoMapper.selectByExample(example);
        if (labelPos.size() > 1) {
            return true;
        } else if (labelPos.size() == 1) {
            // 閺囧瓨鏌婇弮绉巃belId閺勵垱婀侀崐鑲╂畱
            return !labelPos.get(0).getId().equals(labelId);
        }
        return false;
    }

}


