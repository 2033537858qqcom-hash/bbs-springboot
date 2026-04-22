package com.liang.bbs.article.facade.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class InternalTimeRangeRequest implements Serializable {

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private static final long serialVersionUID = 1L;
}
