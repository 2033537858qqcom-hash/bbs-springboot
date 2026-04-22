package com.liang.bbs.article.persistence.mapper;

import com.liang.bbs.article.persistence.entity.CommentPo;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 */
public interface CommentPoExMapper {

    /**
     * 鑾峰彇鏈€鏂拌瘎璁轰俊鎭?
     *
     * @param content
     * @param commentUser
     * @return
     */
    List<CommentPo> selectLatestComments(String content, Long commentUser);

    /**
     * 鑾峰彇鎵€鏈夐€氳繃瀹℃牳鏂囩珷鐨勮瘎璁轰俊鎭?
     *
     * @param startTime
     * @param endTime
     * @return
     */
    List<CommentPo> getAllArticleComment(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    /**
     * 鑾峰彇鎵€鏈夐€氳繃瀹℃牳鏂囩珷鐨勮瘎璁哄洖澶嶄俊鎭?
     *
     * @param startTime
     * @param endTime
     * @return
     */
    List<CommentPo> getAllCommentReply(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

}
