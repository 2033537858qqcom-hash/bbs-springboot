package com.liang.bbs.user.facade.server;

import com.github.pagehelper.PageInfo;
import com.liang.bbs.user.facade.dto.FollowCountDTO;
import com.liang.bbs.user.facade.dto.FollowDTO;
import com.liang.bbs.user.facade.dto.FollowSearchDTO;
import com.liang.nansheng.common.auth.UserSsoDTO;

import java.time.LocalDateTime;
import java.util.List;

/**
 */
public interface FollowService {

    /**
     * 鑾峰彇鎵€鏈夌殑鍏虫敞
     *
     * @param startTime
     * @param endTime
     * @return
     */
    List<FollowDTO> getPaasAll(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 鑾峰彇鍏虫敞鐨勭敤鎴蜂俊鎭?
     *
     * @param followSearchDTO
     * @param currentUser
     * @return
     */
    PageInfo<FollowDTO> getFollowUsers(FollowSearchDTO followSearchDTO, UserSsoDTO currentUser);

    /**
     * 閫氳繃id鑾峰彇鍏虫敞淇℃伅
     *
     * @param id
     * @return
     */
    FollowDTO getById(Integer id);

    /**
     * 閫氳繃fromUser鍜宼oUser鑾峰彇鍏虫敞淇℃伅
     *
     * @param fromUser
     * @param toUser
     * @param isAll true:涓嶅尯鍒嗗叧娉ㄤ笌鍚︼紝false:鍙煡璇㈠叧娉ㄤ簡鐨?
     * @return
     */
    FollowDTO getByFromToUser(Long fromUser, Long toUser, Boolean isAll);

    /**
     * 鏇存柊鍏虫敞鐘舵€?
     *
     * @param fromUser
     * @param toUser
     * @return
     */
    Boolean updateFollowState(Long fromUser, Long toUser);

    /**
     * 鑾峰彇鍏虫敞/绮変笣鏁伴噺
     *
     * @param userId
     * @return
     */
    FollowCountDTO getFollowCount(Long userId);

}
