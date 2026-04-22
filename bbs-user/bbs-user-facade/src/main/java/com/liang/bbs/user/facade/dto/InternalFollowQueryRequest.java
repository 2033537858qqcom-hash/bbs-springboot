package com.liang.bbs.user.facade.dto;

import com.liang.nansheng.common.auth.UserSsoDTO;
import lombok.Data;

import java.io.Serializable;

@Data
public class InternalFollowQueryRequest implements Serializable {

    private FollowSearchDTO followSearchDTO;

    private UserSsoDTO currentUser;

    private static final long serialVersionUID = 1L;
}
