package com.liang.local.auth.mapper;

import com.liang.local.auth.entity.Visit;
import org.apache.ibatis.annotations.*;

/**
 * 访问记录Mapper
 */
@Mapper
public interface VisitMapper {
    
    /**
     * 插入访问记录
     */
    @Insert("INSERT INTO local_visit(project_id, ip, os, create_time) " +
            "VALUES(#{projectId}, #{ip}, #{os}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Visit visit);
    
    /**
     * 统计总访问量
     */
    @Select("SELECT COUNT(*) FROM local_visit")
    long countTotal();
    
    /**
     * 统计今日访问量
     */
    @Select("SELECT COUNT(*) FROM local_visit WHERE DATE(create_time) = CURDATE()")
    long countToday();
}
