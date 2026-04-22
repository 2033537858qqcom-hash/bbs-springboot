package com.liang.bbs.article.facade.dto;

import com.liang.nansheng.common.auth.UserSsoDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class InternalArticleIdsRequest implements Serializable {

    private List<Integer> ids;

    private Boolean isPv;

    private UserSsoDTO currentUser;

    private static final long serialVersionUID = 1L;
}
