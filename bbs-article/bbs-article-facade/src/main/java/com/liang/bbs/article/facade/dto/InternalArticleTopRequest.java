package com.liang.bbs.article.facade.dto;

import com.liang.nansheng.common.auth.UserSsoDTO;
import lombok.Data;

import java.io.Serializable;

@Data
public class InternalArticleTopRequest implements Serializable {

    private Integer id;

    private Boolean top;

    private UserSsoDTO currentUser;

    private static final long serialVersionUID = 1L;
}
