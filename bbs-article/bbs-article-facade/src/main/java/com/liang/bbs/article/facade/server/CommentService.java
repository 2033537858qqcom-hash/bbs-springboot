package com.liang.bbs.article.facade.server;

import com.github.pagehelper.PageInfo;
import com.liang.bbs.article.facade.dto.CommentDTO;
import com.liang.bbs.article.facade.dto.CommentSearchDTO;
import com.liang.nansheng.common.auth.UserSsoDTO;

import java.time.LocalDateTime;
import java.util.List;

/**
 */
public interface CommentService {
    /**
     * 鑾峰彇鏂囩珷鐨勮瘎璁轰俊鎭?
     *
     * @param commentSearchDTO
     * @return
     */
    List<CommentDTO> getCommentByArticleId(CommentSearchDTO commentSearchDTO, UserSsoDTO currentUser);

    /**
     * 鑾峰彇鎵€鏈夐€氳繃瀹℃牳鏂囩珷鐨勮瘎璁轰俊鎭?
     *
     * @param startTime
     * @param endTime
     * @return
     */
    List<CommentDTO> getAllArticleComment(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 鑾峰彇鎵€鏈夐€氳繃瀹℃牳鏂囩珷鐨勮瘎璁哄洖澶嶄俊鎭?
     *
     * @param startTime
     * @param endTime
     * @return
     */
    List<CommentDTO> getAllCommentReply(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 鑾峰彇鏈€鏂拌瘎璁轰俊鎭?
     *
     * @param commentSearchDTO
     * @return
     */
    PageInfo<CommentDTO> getLatestComment(CommentSearchDTO commentSearchDTO);

    /**
     * 鑾峰彇鏂囩珷鐨勮瘎璁烘暟閲?
     *
     * @param articleId
     * @return
     */
    Long getCommentCountByArticle(Integer articleId);

    /**
     * 鑾峰彇璇勮鏁伴噺
     *
     * @return
     */
    Long getTotal();

    /**
     * 鍒涘缓璇勮
     *
     * @param commentDTO
     * @param currentUser
     * @return
     */
    Boolean create(CommentDTO commentDTO, UserSsoDTO currentUser);

    /**
     * 鍒犻櫎璇勮
     *
     * @param commentId
     * @return
     */
    Boolean delete(Integer commentId);

    /**
     * 閫氳繃鐖剁骇ID鑾峰彇瀛愮骇璇勮淇℃伅
     *
     * @param result 瀛樻斁缁撴灉
     * @param preId
     * @return
     */
    void getAllChildrenByPreId(List<CommentDTO> result, Integer preId);

    /**
     * 鑾峰彇璇勮id鑾峰彇鏂囩珷id
     *
     * @param commentId
     * @return
     */
    Integer getArticleIdByCommentId(Integer commentId);

    /**
     * 閫氳繃鎵归噺id鑾峰彇璇勮淇℃伅
     *
     * @param commentId
     * @return
     */
    CommentDTO getById(Integer commentId);
}
