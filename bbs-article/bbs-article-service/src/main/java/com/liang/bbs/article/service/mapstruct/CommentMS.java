package com.liang.bbs.article.service.mapstruct;

import com.liang.bbs.article.facade.dto.CommentDTO;
import com.liang.bbs.article.persistence.entity.CommentPo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 */
@Mapper(componentModel = "spring")
public interface CommentMS extends CommonMS<CommentPo, CommentDTO> {
    CommentMS INSTANCE = Mappers.getMapper(CommentMS.class);
}
