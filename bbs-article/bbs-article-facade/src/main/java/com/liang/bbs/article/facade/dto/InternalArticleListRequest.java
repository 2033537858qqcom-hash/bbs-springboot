package com.liang.bbs.article.facade.dto;

import com.liang.bbs.common.enums.ArticleStateEnum;
import com.liang.nansheng.common.auth.UserSsoDTO;
import lombok.Data;

import java.io.Serializable;

@Data
public class InternalArticleListRequest implements Serializable {

    private ArticleSearchDTO articleSearchDTO;

    private UserSsoDTO currentUser;

    private ArticleStateEnum articleStateEnum;

    private static final long serialVersionUID = 1L;
}
