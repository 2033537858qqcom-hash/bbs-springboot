package com.liang.bbs.article.service.mapstruct;

import com.liang.bbs.article.facade.dto.SlideshowDTO;
import com.liang.bbs.article.persistence.entity.SlideshowPo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 */
@Mapper(componentModel = "spring")
public interface SlideshowMS extends CommonMS<SlideshowPo, SlideshowDTO> {
    SlideshowMS INSTANCE = Mappers.getMapper(SlideshowMS.class);
}
