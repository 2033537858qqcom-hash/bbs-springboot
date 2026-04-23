package com.liang.local.auth.mapper;

import com.liang.local.auth.entity.User;
import org.apache.ibatis.annotations.*;
import java.util.List;

/**
 * 用户Mapper
 */
@Mapper
public interface UserMapper {
    
    /**
     * 根据ID查询用户
     */
    @Select("SELECT * FROM local_user WHERE id = #{id}")
    User getById(Long id);
    
    /**
     * 根据ID列表批量查询用户
     */
    @Select("<script>" +
            "SELECT * FROM local_user WHERE id IN " +
            "<foreach item='id' collection='userIds' open='(' separator=',' close=')'>" +
            "#{id}" +
            "</foreach>" +
            "</script>")
    List<User> getByIds(@Param("userIds") List<Long> userIds);
    
    /**
     * 根据用户名查询
     */
    @Select("SELECT * FROM local_user WHERE username = #{username}")
    User getByUsername(String username);
    
    /**
     * 根据邮箱查询
     */
    @Select("SELECT * FROM local_user WHERE email = #{email}")
    User getByEmail(String email);
    
    /**
     * 根据手机号查询
     */
    @Select("SELECT * FROM local_user WHERE phone = #{phone}")
    User getByPhone(String phone);
    
    /**
     * 插入用户
     */
    @Insert("INSERT INTO local_user(username, email, phone, password, avatar, state, create_time, update_time) " +
            "VALUES(#{username}, #{email}, #{phone}, #{password}, #{avatar}, #{state}, NOW(), NOW())")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(User user);
    
    /**
     * 更新用户信息
     */
    @Update("UPDATE local_user SET username=#{username}, email=#{email}, phone=#{phone}, " +
            "avatar=#{avatar}, state=#{state}, update_time=NOW() WHERE id=#{id}")
    int update(User user);
    
    /**
     * 更新密码
     */
    @Update("UPDATE local_user SET password=#{password}, update_time=NOW() WHERE id=#{id}")
    int updatePassword(@Param("id") Long id, @Param("password") String password);
    
    /**
     * 检查邮箱是否存在
     */
    @Select("SELECT COUNT(*) FROM local_user WHERE email = #{email}")
    int countByEmail(String email);
    
    /**
     * 检查手机号是否存在
     */
    @Select("SELECT COUNT(*) FROM local_user WHERE phone = #{phone}")
    int countByPhone(String phone);
    
    /**
     * 检查用户名是否存在
     */
    @Select("SELECT COUNT(*) FROM local_user WHERE username = #{username}")
    int countByUsername(String username);
    
    /**
     * 获取所有用户列表
     */
    @Select("SELECT * FROM local_user WHERE state = 1 ORDER BY id")
    List<User> getAllList();
}
