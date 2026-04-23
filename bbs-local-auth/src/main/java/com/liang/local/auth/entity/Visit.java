package com.liang.local.auth.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 访问记录实体类
 */
@Data
public class Visit {
    /**
     * 记录ID
     */
    private Long id;
    
    /**
     * 项目ID
     */
    private Integer projectId;
    
    /**
     * IP地址
     */
    private String ip;
    
    /**
     * 操作系统
     */
    private String os;
    
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
}
