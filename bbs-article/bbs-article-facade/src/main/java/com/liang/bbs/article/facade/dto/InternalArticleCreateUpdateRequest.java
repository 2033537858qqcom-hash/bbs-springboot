package com.liang.bbs.article.facade.dto;

import com.liang.nansheng.common.auth.UserSsoDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class InternalArticleCreateUpdateRequest implements Serializable {

    private byte[] bytes;

    private String sourceFileName;

    private ArticleDTO articleDTO;

    private List<Integer> labelIds;

    private UserSsoDTO currentUser;

    private static final long serialVersionUID = 1L;
}
