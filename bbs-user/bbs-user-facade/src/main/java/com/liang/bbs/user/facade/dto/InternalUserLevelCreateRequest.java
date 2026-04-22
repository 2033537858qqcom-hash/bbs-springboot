package com.liang.bbs.user.facade.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class InternalUserLevelCreateRequest implements Serializable {

    private Long userId;

    private static final long serialVersionUID = 1L;
}
