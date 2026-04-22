package com.liang.bbs.user.facade.dto;

import com.liang.nansheng.common.auth.UserSsoDTO;
import lombok.Data;

import java.io.Serializable;

@Data
public class InternalLikeOperateRequest implements Serializable {

    private Integer articleId;

    private UserSsoDTO currentUser;

    private static final long serialVersionUID = 1L;
}
