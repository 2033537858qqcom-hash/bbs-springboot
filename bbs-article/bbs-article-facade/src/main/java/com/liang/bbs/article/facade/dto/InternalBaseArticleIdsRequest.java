package com.liang.bbs.article.facade.dto;

import com.liang.bbs.common.enums.ArticleStateEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class InternalBaseArticleIdsRequest implements Serializable {

    private List<Integer> ids;

    private ArticleStateEnum articleStateEnum;

    private static final long serialVersionUID = 1L;
}
