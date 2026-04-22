package com.liang.bbs.user.facade.server;

import com.github.pagehelper.PageInfo;
import com.liang.bbs.user.facade.dto.UserForumDTO;
import com.liang.bbs.user.facade.dto.UserLevelDTO;
import com.liang.bbs.user.facade.dto.UserSearchDTO;
import com.liang.nansheng.common.auth.UserSsoDTO;

import java.util.List;

/**
 */
public interface UserLevelService {
    /**
     * 鍒涘缓鐢ㄦ埛绛夌骇淇℃伅
     *
     * @param userId
     * @return
     */
    Boolean create(Long userId);

    /**
     * 鏇存柊鐢ㄦ埛绛夌骇淇℃伅
     *
     * @param userId 鐢ㄦ埛id
     * @param points 绉垎
     * @return
     */
    Boolean update(Long userId,  Integer points);

    /**
     * 鏇存柊鎵€鏈夌敤鎴风瓑绾т俊鎭?
     *
     * @return
     */
    Boolean updatePointsAll();

    /**
     * 鍚屾鎵€鏈夌敤鎴风瓑绾т俊鎭?
     *
     * @return
     */
    Boolean syncAll();

    /**
     * 鑾峰彇鐑棬浣滆€呭垪琛?
     *
     * @param userSearchDTO
     * @param currentUser
     * @return
     */
    PageInfo<UserForumDTO> getHotAuthorsList(UserSearchDTO userSearchDTO, UserSsoDTO currentUser);

    /**
     * 閫氳繃鐢ㄦ埛id鑾峰彇鐢ㄦ埛绛夌骇淇℃伅
     *
     * @param userId
     * @return
     */
    List<UserLevelDTO> getByUserId(Long userId);

    /**
     * 閫氳繃鐢ㄦ埛id闆嗗悎鑾峰彇鐢ㄦ埛绛夌骇淇℃伅
     *
     * @param userIds
     * @return
     */
    List<UserLevelDTO> getByUserIds(List<Long> userIds);

    /**
     * 鑾峰彇鐢ㄦ埛淇℃伅
     *
     * @param userId
     * @param currentUser
     * @return
     */
    UserForumDTO getUserInfo(Long userId, UserSsoDTO currentUser);
}
