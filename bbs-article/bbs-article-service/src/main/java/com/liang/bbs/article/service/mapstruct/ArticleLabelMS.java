package com.liang.bbs.article.service.mapstruct;

import com.liang.bbs.article.facade.dto.ArticleLabelDTO;
import com.liang.bbs.article.persistence.entity.ArticleLabelPo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 */
@Mapper(componentModel = "spring")
public interface ArticleLabelMS extends CommonMS<ArticleLabelPo, ArticleLabelDTO> {
    ArticleLabelMS INSTANCE = Mappers.getMapper(ArticleLabelMS.class);
}
