package com.liang.bbs.article.facade.server;

import com.liang.bbs.article.facade.dto.ArticleLabelDTO;
import com.liang.nansheng.common.auth.UserSsoDTO;

import java.util.List;

/**
 */
public interface ArticleLabelService {
    /**
     * 鏂板鏂囦欢鏍囩鍏崇郴淇℃伅
     *
     * @param labelIds
     * @param articleId
     * @param currentUser
     * @return
     */
    Boolean create(List<Integer> labelIds, Integer articleId, UserSsoDTO currentUser);

    /**
     * 鏇存柊鏂囦欢鏍囩鍏崇郴淇℃伅
     *
     * @param labelIds
     * @param articleId
     * @param currentUser
     * @return
     */
    Boolean update(List<Integer> labelIds, Integer articleId, UserSsoDTO currentUser);

    /**
     * 鏍规嵁鏍囩id闆嗗悎鑾峰彇鏂囩珷鏍囩淇℃伅
     *
     * @param labelIds
     * @return
     */
    List<ArticleLabelDTO> getByLabelIds(List<Integer> labelIds);

    /**
     * 鏍规嵁鏂囩珷id闆嗗悎鑾峰彇鏂囩珷鏍囩淇℃伅
     *
     * @param articleIds
     * @return
     */
    List<ArticleLabelDTO> getByArticleIds(List<Integer> articleIds);

    /**
     * 鑾峰彇鏍囩浣跨敤鏁伴噺
     *
     * @param labelId
     * @return
     */
    Long getCountByLabelId(Integer labelId);

}
