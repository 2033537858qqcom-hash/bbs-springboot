package com.liang.local.auth.mapper;

import com.liang.local.auth.entity.Notify;
import org.apache.ibatis.annotations.*;
import java.util.List;

/**
 * 通知Mapper
 */
@Mapper
public interface NotifyMapper {
    
    /**
     * 插入通知
     */
    @Insert("INSERT INTO local_notify(user_id, type, title, content, is_read, create_time) " +
            "VALUES(#{userId}, #{type}, #{title}, #{content}, #{isRead}, NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Notify notify);
    
    /**
     * 根据用户ID和类型查询通知列表
     */
    @Select("SELECT * FROM local_notify WHERE user_id = #{userId} AND type = #{type} " +
            "ORDER BY create_time DESC LIMIT #{offset}, #{limit}")
    List<Notify> getList(@Param("userId") Long userId, 
                        @Param("type") Integer type,
                        @Param("offset") int offset,
                        @Param("limit") int limit);
    
    /**
     * 统计未读数量
     */
    @Select("SELECT COUNT(*) FROM local_notify WHERE user_id = #{userId} AND type = #{type} AND is_read = 0")
    int countUnread(@Param("userId") Long userId, @Param("type") Integer type);
    
    /**
     * 标记已读
     */
    @Update("UPDATE local_notify SET is_read = 1 WHERE id = #{id}")
    int markRead(Long id);
    
    /**
     * 批量标记已读
     */
    @Update("<script>" +
            "UPDATE local_notify SET is_read = 1 WHERE id IN " +
            "<foreach item='id' collection='notifyIds' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            "</script>")
    int batchMarkRead(@Param("notifyIds") List<Integer> notifyIds);
}
