package com.liang.bbs.article.service.mapstruct;

import com.liang.bbs.article.facade.dto.LabelDTO;
import com.liang.bbs.article.persistence.entity.LabelPo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 */
@Mapper(componentModel = "spring")
public interface LabelMS extends CommonMS<LabelPo, LabelDTO> {
    LabelMS INSTANCE = Mappers.getMapper(LabelMS.class);
}
