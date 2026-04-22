package com.liang.bbs.article.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.liang.bbs.article.facade.dto.ResourceNavigateDTO;
import com.liang.bbs.article.facade.dto.ResourceNavigateSearchDTO;
import com.liang.bbs.article.facade.server.ResourceNavigateService;
import com.liang.bbs.article.persistence.entity.ResourceNavigatePo;
import com.liang.bbs.article.persistence.entity.ResourceNavigatePoExample;
import com.liang.bbs.article.persistence.mapper.ResourceNavigatePoMapper;
import com.liang.bbs.article.service.client.FileServiceClient;
import com.liang.bbs.article.service.mapstruct.ResourceNavigateMS;
import com.liang.nansheng.common.auth.UserSsoDTO;
import com.liang.nansheng.common.enums.ImageTypeEnum;
import com.liang.nansheng.common.enums.ResponseCode;
import com.liang.nansheng.common.web.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 */
@Slf4j
@Service
public class ResourceNavigateServiceImpl implements ResourceNavigateService {
    @Autowired
    private ResourceNavigatePoMapper resourceNavigatePoMapper;

    @Autowired
    private FileServiceClient fileService;

    /**
     * 閼惧嘲褰囩挧鍕爱鐎佃壈鍩?
     *
     * @param resourceNavigateSearchDTO
     * @return
     */
    @Override
    public PageInfo<ResourceNavigateDTO> getList(ResourceNavigateSearchDTO resourceNavigateSearchDTO) {
        ResourceNavigatePoExample example = new ResourceNavigatePoExample();
        ResourceNavigatePoExample.Criteria criteria = example.createCriteria().andIsDeletedEqualTo(false);
        if (StringUtils.isNotBlank(resourceNavigateSearchDTO.getCategory())) {
            criteria.andCategoryEqualTo(resourceNavigateSearchDTO.getCategory());
        }
        example.setOrderByClause("`id` desc");
        PageHelper.startPage(resourceNavigateSearchDTO.getCurrentPage(), resourceNavigateSearchDTO.getPageSize());

        return ResourceNavigateMS.INSTANCE.toPage(new PageInfo<>(resourceNavigatePoMapper.selectByExample(example)));
    }

    /**
     * 閺傛澘顤冪挧鍕爱鐎佃壈鍩?
     *
     * @param resourceNavigateDTO
     * @param currentUser
     * @return
     */
    @Override
    public Boolean create(ResourceNavigateDTO resourceNavigateDTO, UserSsoDTO currentUser) {
        if (StringUtils.isBlank(resourceNavigateDTO.getResourceName()) ||
                StringUtils.isBlank(resourceNavigateDTO.getCategory()) ||
                StringUtils.isBlank(resourceNavigateDTO.getDesc()) ||
                StringUtils.isBlank(resourceNavigateDTO.getLink())) {
            throw BusinessException.build(ResponseCode.NOT_EXISTS, "资源名称、分类、描述和链接不能为空");
        }
        if (isNameExist(null, resourceNavigateDTO.getResourceName())) {
            throw BusinessException.build(ResponseCode.NAME_EXIST, "资源名称已存在");
        }

        resourceNavigateDTO.setIsDeleted(false);
        resourceNavigateDTO.setCreateUser(currentUser.getUserId());
        resourceNavigateDTO.setUpdateUser(currentUser.getUserId());
        LocalDateTime now = LocalDateTime.now();
        resourceNavigateDTO.setCreateTime(now);
        resourceNavigateDTO.setUpdateTime(now);
        ResourceNavigatePo resourceNavigatePo = ResourceNavigateMS.INSTANCE.toPo(resourceNavigateDTO);
        if (resourceNavigatePoMapper.insertSelective(resourceNavigatePo) <= 0) {
            throw BusinessException.build(ResponseCode.OPERATE_FAIL, "閺傛澘顤冪挧鍕爱鐎佃壈鍩呮径杈Е");
        }

        return true;
    }

    /**
     * 娑撳﹣绱剁挧鍕爱鐎佃壈鍩卨ogo
     *
     * @param bytes
     * @param sourceFileName
     * @return
     */
    @Override
    public String uploadResourceNavigateLogo(byte[] bytes, String sourceFileName) {
        try {
            // 閺傚洣娆㈡稉濠佺炊閿涘牆澹€閸掑浄绱?
            return fileService.fileCutUpload(bytes, sourceFileName, ImageTypeEnum.resourceNavigatePicture.name());
        } catch (Exception e) {
            log.error("上传资源导航图标失败", e);
            throw BusinessException.build(ResponseCode.OPERATE_FAIL, "上传资源导航图标失败");
        }
    }

    /**
     * 閺囧瓨鏌婄挧鍕爱鐎佃壈鍩?
     *
     * @param resourceNavigateDTO
     * @param currentUser
     * @return
     */
    @Override
    public Boolean update(ResourceNavigateDTO resourceNavigateDTO, UserSsoDTO currentUser) {
        if (StringUtils.isBlank(resourceNavigateDTO.getResourceName()) ||
                StringUtils.isBlank(resourceNavigateDTO.getCategory()) ||
                StringUtils.isBlank(resourceNavigateDTO.getDesc()) ||
                StringUtils.isBlank(resourceNavigateDTO.getLink())) {
            throw BusinessException.build(ResponseCode.NOT_EXISTS, "资源名称、分类、描述和链接不能为空");
        }
        if (isNameExist(resourceNavigateDTO.getId(), resourceNavigateDTO.getResourceName())) {
            throw BusinessException.build(ResponseCode.NAME_EXIST, "资源名称已存在");
        }
        resourceNavigateDTO.setIsDeleted(null);
        resourceNavigateDTO.setCreateUser(null);
        resourceNavigateDTO.setUpdateUser(currentUser.getUserId());
        resourceNavigateDTO.setCreateTime(null);
        resourceNavigateDTO.setUpdateTime(LocalDateTime.now());
        ResourceNavigatePo resourceNavigatePo = ResourceNavigateMS.INSTANCE.toPo(resourceNavigateDTO);
        if (resourceNavigatePoMapper.updateByPrimaryKeySelective(resourceNavigatePo) <= 0) {
            throw BusinessException.build(ResponseCode.OPERATE_FAIL, "閺囧瓨鏌婄挧鍕爱鐎佃壈鍩呮径杈Е");
        }

        return true;
    }

    /**
     * 閸掔娀娅庣挧鍕爱鐎佃壈鍩?
     *
     * @param id
     * @return
     */
    @Override
    public Boolean delete(Integer id) {
        ResourceNavigatePo resourceNavigatePo = new ResourceNavigatePo();
        resourceNavigatePo.setId(id);
        resourceNavigatePo.setIsDeleted(true);
        if (resourceNavigatePoMapper.updateByPrimaryKeySelective(resourceNavigatePo) <= 0) {
            throw BusinessException.build(ResponseCode.OPERATE_FAIL, "閸掔娀娅庢径杈Е");
        }

        return true;
    }

    /**
     * 閼惧嘲褰囩挧鍕爱鐎佃壈鍩呴幍鈧張澶岃閸?
     *
     * @return
     */
    @Override
    public List<String> getCategorys() {
        List<String> categorys = new ArrayList<>();
        ResourceNavigatePoExample example = new ResourceNavigatePoExample();
        example.createCriteria().andIsDeletedEqualTo(false);
        List<ResourceNavigateDTO> resourceNavigateDTOS = ResourceNavigateMS.INSTANCE.toDTO(resourceNavigatePoMapper.selectByExample(example));
        if (CollectionUtils.isNotEmpty(resourceNavigateDTOS)) {
            categorys = resourceNavigateDTOS.stream().map(ResourceNavigateDTO::getCategory).distinct().collect(Collectors.toList());
        }
        return categorys;
    }

    /**
     * 閸掋倖鏌囩挧鍕爱鐎佃壈鍩呴崥宥囆為弰顖氭儊瀹歌尙绮＄€涙ê婀?
     *
     * @param resourceId   鐠у嫭绨€佃壈鍩卛d
     * @param resourceName 鐠у嫭绨€佃壈鍩呴崥宥囆?
     * @return
     */
    private boolean isNameExist(Integer resourceId, String resourceName) {
        ResourceNavigatePoExample example = new ResourceNavigatePoExample();
        example.createCriteria().andIsDeletedEqualTo(false)
                .andResourceNameEqualTo(resourceName);
        List<ResourceNavigatePo> resourceNavigatePos = resourceNavigatePoMapper.selectByExample(example);
        if (resourceNavigatePos.size() > 1) {
            return true;
        } else if (resourceNavigatePos.size() == 1) {
            // 閺囧瓨鏌婇弮绉巃belId閺勵垱婀侀崐鑲╂畱
            return !resourceNavigatePos.get(0).getId().equals(resourceId);
        }
        return false;
    }

}


