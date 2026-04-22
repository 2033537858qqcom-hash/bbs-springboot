package com.liang.bbs.article.facade.dto;

import com.liang.bbs.user.facade.dto.LikeSearchDTO;
import com.liang.nansheng.common.auth.UserSsoDTO;
import lombok.Data;

import java.io.Serializable;

@Data
public class InternalLikeSearchRequest implements Serializable {

    private LikeSearchDTO likeSearchDTO;

    private UserSsoDTO currentUser;

    private static final long serialVersionUID = 1L;
}
