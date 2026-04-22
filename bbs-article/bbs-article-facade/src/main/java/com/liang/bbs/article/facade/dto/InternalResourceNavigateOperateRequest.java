package com.liang.bbs.article.facade.dto;

import com.liang.nansheng.common.auth.UserSsoDTO;
import lombok.Data;

import java.io.Serializable;

@Data
public class InternalResourceNavigateOperateRequest implements Serializable {

    private ResourceNavigateDTO resourceNavigateDTO;

    private UserSsoDTO currentUser;

    private static final long serialVersionUID = 1L;
}
