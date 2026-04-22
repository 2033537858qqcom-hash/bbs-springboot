package com.liang.bbs.article.service.mapstruct;

import com.liang.bbs.article.facade.dto.ArticleDTO;
import com.liang.bbs.article.persistence.entity.ArticlePo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 */
@Mapper(componentModel = "spring")
public interface ArticleMS extends CommonMS<ArticlePo, ArticleDTO> {
    ArticleMS INSTANCE = Mappers.getMapper(ArticleMS.class);
}
