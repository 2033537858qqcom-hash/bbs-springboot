package com.liang.bbs.user.persistence.mapper;

import com.liang.bbs.user.persistence.entity.LikeCommentPo;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 */
public interface LikeCommentPoExMapper {

    /**
     * 鑾峰彇鎵€鏈夐€氳繃瀹℃牳鏂囩珷鐨勮瘎璁虹殑鐐硅禐淇℃伅
     *
     * @param startTime
     * @param endTime
     * @return
     */
    List<LikeCommentPo> selectAllCommentLike(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

}
