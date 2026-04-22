package com.liang.bbs.user.facade.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class InternalFollowStateRequest implements Serializable {

    private Long fromUser;

    private Long toUser;

    private static final long serialVersionUID = 1L;
}
