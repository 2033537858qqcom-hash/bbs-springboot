package com.liang.bbs.user.facade.server;

import com.liang.bbs.user.facade.dto.LikeCommentDTO;
import com.liang.nansheng.common.auth.UserSsoDTO;

import java.time.LocalDateTime;
import java.util.List;

/**
 */
public interface LikeCommentService {

    /**
     * 鑾峰彇鎵€鏈夐€氳繃瀹℃牳鏂囩珷鐨勮瘎璁虹殑鐐硅禐淇℃伅
     *
     * @param startTime
     * @param endTime
     * @return
     */
    List<LikeCommentDTO> getPaasAll(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 鑾峰彇璇勮鐨勭偣璧炴暟閲?
     *
     * @param commentId
     * @return
     */
    Long getLikeCountCommentId(Integer commentId);

    /**
     * 鏄惁鐐硅禐
     *
     * @param commentId
     * @param userId
     * @return
     */
    Boolean isLike(Integer commentId, Long userId);

    /**
     * 鏇存柊鐐硅禐鐘舵€?
     *
     * @param commentId
     * @param currentUser
     * @return
     */
    Boolean updateLikeCommentState(Integer commentId, UserSsoDTO currentUser);

    /**
     * 閫氳繃璇勮id鍜岀敤鎴穒d鑾峰彇鐐硅禐淇℃伅
     *
     * @param commentId
     * @param userId
     * @return
     */
    LikeCommentDTO getByCommentIdUserId(Integer commentId, Long userId);

}
