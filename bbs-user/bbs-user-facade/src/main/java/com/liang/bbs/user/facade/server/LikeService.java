package com.liang.bbs.user.facade.server;

import com.github.pagehelper.PageInfo;
import com.liang.bbs.user.facade.dto.LikeDTO;
import com.liang.bbs.user.facade.dto.LikeSearchDTO;
import com.liang.nansheng.common.auth.UserSsoDTO;

import java.time.LocalDateTime;
import java.util.List;

/**
 */
public interface LikeService {

    /**
     * 鑾峰彇鎵€鏈夌偣璧炵殑閫氳繃瀹℃牳鐨勬枃绔犱俊鎭?
     *
     * @param startTime
     * @param endTime
     * @return
     */
    List<LikeDTO> getPaasAll(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 閫氳繃鐢ㄦ埛id鑾峰彇鐐硅禐鐨勬枃绔犱俊鎭?
     *
     * @param likeSearchDTO
     * @return
     */
    PageInfo<LikeDTO> getArticleByUserId(LikeSearchDTO likeSearchDTO);

    /**
     * 閫氳繃鏂囩珷id鑾峰彇鐐硅禐鐨勭敤鎴蜂俊鎭?
     *
     * @param likeSearchDTO
     * @return
     */
    PageInfo<LikeDTO> getUserByArticleId(LikeSearchDTO likeSearchDTO);

    /**
     * 閫氳繃id鑾峰彇鐐硅禐淇℃伅
     *
     * @param id
     * @return
     */
    LikeDTO getById(Integer id);

    /**
     * 閫氳繃鏂囩珷id鍜岀敤鎴穒d鑾峰彇鐐硅禐淇℃伅
     *
     * @param articleId
     * @param userId
     * @return
     */
    LikeDTO getByArticleIdUserId(Integer articleId, Long userId);

    /**
     * 鑾峰彇鏂囩珷鐨勭偣璧炴暟閲?
     *
     * @param articleIds
     * @return
     */
    Long getLikeCountArticle(List<Integer> articleIds);

    /**
     * 鏄惁鐐硅禐
     *
     * @param articleId
     * @param userId
     * @return
     */
    Boolean isLike(Integer articleId, Long userId);

    /**
     * 鏇存柊鐐硅禐鐘舵€?
     *
     * @param articleId
     * @param currentUser
     * @return
     */
    Boolean updateLikeState(Integer articleId, UserSsoDTO currentUser);

    /**
     * 鐢ㄦ埛鑾峰彇鐨勭偣璧炴暟閲?
     *
     * @param userId
     * @return
     */
    Long getUserLikeCount(Long userId);

    /**
     * 鐢ㄦ埛鑾风偣璧炵殑鏁伴噺
     *
     * @param userId
     * @return
     */
    Long getUserTheLikeCount(Long userId);

}
