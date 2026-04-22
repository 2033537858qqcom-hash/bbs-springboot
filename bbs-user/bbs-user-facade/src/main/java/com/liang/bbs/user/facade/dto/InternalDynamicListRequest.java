package com.liang.bbs.user.facade.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class InternalDynamicListRequest implements Serializable {

    private Long userId;

    private Integer currentPage;

    private Integer pageSize;

    private static final long serialVersionUID = 1L;
}
