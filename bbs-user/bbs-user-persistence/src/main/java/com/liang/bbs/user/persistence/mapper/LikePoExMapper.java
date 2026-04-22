package com.liang.bbs.user.persistence.mapper;

import com.liang.bbs.user.persistence.entity.LikePo;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 */
public interface LikePoExMapper {

    /**
     * 閫氳繃鐢ㄦ埛id鑾峰彇鐐硅禐鐨勬枃绔犱俊鎭?
     *
     * @param likeUser
     * @return
     */
    List<LikePo> selectArticleByUserId(Long likeUser);

    /**
     * 鑾峰彇鐐硅禐鐨勬墍鏈夋枃绔犱俊鎭?
     *
     * @param startTime
     * @param endTime
     * @return
     */
    List<LikePo> selectAllArticle(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

}
