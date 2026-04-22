package com.liang.bbs.article.facade.server;

import com.github.pagehelper.PageInfo;
import com.liang.bbs.article.facade.dto.*;
import com.liang.bbs.common.enums.ArticleStateEnum;
import com.liang.bbs.user.facade.dto.LikeSearchDTO;
import com.liang.nansheng.common.auth.UserSsoDTO;

import java.time.LocalDateTime;
import java.util.List;

/**
 */
public interface ArticleService {

    /**
     * 鑾峰彇鎵€鏈夊鏍搁€氳繃鐨勬枃绔?
     *
     * @param startTime
     * @param endTime
     * @return
     */
    List<ArticleDTO> getPassAll(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 鑾峰彇鏂囩珷
     *
     * @param articleSearchDTO
     * @param currentUser
     * @param articleStateEnum
     * @return
     */
    PageInfo<ArticleDTO> getList(ArticleSearchDTO articleSearchDTO, UserSsoDTO currentUser, ArticleStateEnum articleStateEnum);

    /**
     * 鑾峰彇鐢ㄦ埛鏂囩珷鏁伴噺
     *
     * @param createUser
     * @param articleStateEnum
     * @return
     */
    Long getUserArticleCount(Long createUser, ArticleStateEnum articleStateEnum);

    /**
     * 鑾峰彇寰呭鏍哥殑鏂囩珷
     *
     * @param articleSearchDTO
     * @param currentUser
     * @return
     */
    PageInfo<ArticleDTO> getPendingReviewArticles(ArticleSearchDTO articleSearchDTO, UserSsoDTO currentUser);

    /**
     * 鑾峰彇绂佺敤鐨勬枃绔?
     *
     * @param articleSearchDTO
     * @param currentUser
     * @return
     */
    PageInfo<ArticleDTO> getDisabledArticles(ArticleSearchDTO articleSearchDTO, UserSsoDTO currentUser);

    /**
     * 淇敼鏂囩珷瀹℃壒鐘舵€?
     *
     * @param articleDTO
     * @param currentUser
     * @return
     */
    Boolean updateState(ArticleDTO articleDTO, UserSsoDTO currentUser);

    /**
     * 鑾峰彇鐐硅禐杩囩殑鏂囩珷
     *
     * @param likeSearchDTO
     * @param currentUser
     * @return
     */
    PageInfo<ArticleDTO> getLikesArticle(LikeSearchDTO likeSearchDTO, UserSsoDTO currentUser);

    /**
     * 閫氳繃鏂囩珷id闆嗗悎鑾峰彇鏂囩珷淇℃伅
     *
     * @param ids
     * @param isPv 鏄惁澧炲姞鏂囩珷娴忚鏁伴噺
     * @param currentUser
     * @return
     */
    List<ArticleDTO> getByIds(List<Integer> ids, Boolean isPv, UserSsoDTO currentUser);

    /**
     * 閫氳繃鏂囩珷id闆嗗悎鑾峰彇鏂囩珷淇℃伅(鏈€鍩虹鐨勪俊鎭?
     *
     * @param ids
     * @return
     */
    List<ArticleDTO> getBaseByIds(List<Integer> ids, ArticleStateEnum articleStateEnum);

    /**
     * 鎾板啓鏂囩珷锛堟棤閰嶅浘锛?
     *
     * @param articleDTO
     * @param currentUser
     */
    Boolean create(ArticleDTO articleDTO, List<Integer> labelIds, UserSsoDTO currentUser);

    /**
     * 鏇存柊鏂囩珷锛堟棤閰嶅浘锛?
     *
     * @param articleDTO
     * @param currentUser
     */
    Boolean update(ArticleDTO articleDTO, List<Integer> labelIds, UserSsoDTO currentUser);

    /**
     * 鎾板啓鏂囩珷
     *
     * @param bytes
     * @param sourceFileName
     * @param articleDTO
     * @param currentUser
     */
    Boolean create(byte[] bytes, String sourceFileName, ArticleDTO articleDTO, List<Integer> labelIds, UserSsoDTO currentUser);

    /**
     * 鏇存柊鏂囩珷
     *
     * @param bytes
     * @param sourceFileName
     * @param articleDTO
     * @param currentUser
     */
    Boolean update(byte[] bytes, String sourceFileName, ArticleDTO articleDTO, List<Integer> labelIds, UserSsoDTO currentUser);

    /**
     * 涓婁紶鍥剧墖锛堜竴寮狅級- mavonEditor
     *
     * @param bytes
     * @param sourceFileName
     * @return
     */
    String uploadPicture(byte[] bytes, String sourceFileName);

    /**
     * 鑾峰彇鏂囩珷璇勮璁块棶鎬绘暟
     *
     * @return
     */
    TotalDTO getArticleCommentVisitTotal();

    /**
     * 鑾峰彇鏂囩珷鏁伴噺
     *
     * @return
     */
    Long getTotal();

    /**
     * 鑾峰彇鐢ㄦ埛闃呰鏁伴噺
     *
     * @param userIds
     * @return
     */
    List<ArticleReadDTO> getUserReadCount(List<Long> userIds);

    /**
     * 鑾峰彇鏂囩珷涓€浜涚粺璁℃暟鎹?
     *
     * @param id
     * @param currentUser
     * @return
     */
    ArticleCountDTO getCountById(Integer id, UserSsoDTO currentUser);

    /**
     * 澧炲姞鏂囩珷娴忚鏁伴噺
     *
     * @param articleDTO
     */
    Boolean updatePv(ArticleDTO articleDTO);

    /**
     * 閫氳繃鐢ㄦ埛鑾峰彇鏂囩珷淇℃伅
     *
     * @param userId
     * @return
     */
    List<ArticleDTO> getByUserId(Long userId);

    /**
     * 鏂囩珷缃《/鍙栨秷缃《
     *
     * @param id
     * @param top 鏄惁缃《锛坱rue锛氱疆椤讹紝false锛氬彇娑堢疆椤讹級
     * @param currentUser
     * @return
     */
    Boolean articleTop(Integer id, Boolean top, UserSsoDTO currentUser);

    /**
     * 鑾峰彇鏂囩珷缃《鐨勬渶澶ф暟鍊?
     *
     * @return
     */
    Integer getMaxTop();

    /**
     * delete
     *
     * @param id
     * @param currentUser
     * @return
     */
    Boolean delete(Integer id, UserSsoDTO currentUser);

    /**
     * 鏂囩珷瀹℃牳鏁版嵁閲?
     *
     * @param title
     * @return
     */
    ArticleCheckCountDTO getArticleCheckCount(String title);
}
