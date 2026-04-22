package com.liang.bbs.user.facade.server;

import com.github.pagehelper.PageInfo;
import com.liang.bbs.user.facade.dto.DynamicDTO;

import java.time.LocalDateTime;

/**
 */
public interface DynamicService {

    /**
     * 鑾峰彇鐢ㄦ埛鐨勫姩鎬佷俊鎭?
     *
     * @param userId
     * @param currentPage
     * @param pageSize
     * @return
     */
    PageInfo<DynamicDTO> getByUserId(Long userId, Integer currentPage, Integer pageSize);

    /**
     * 鍒涘缓鐢ㄦ埛鍔ㄦ€佷俊鎭?
     *
     * @param dynamicDTO
     * @return
     */
    Boolean create(DynamicDTO dynamicDTO);

    /**
     * 楠岃瘉鏄惁宸茬粡瀛樺湪
     *
     * @param dynamicDTO
     * @return
     */
    Boolean verifyExist(DynamicDTO dynamicDTO);

    /**
     * 鍒犻櫎鐢ㄦ埛鍔ㄦ€佷俊鎭?
     *
     * @param startTime
     * @param endTime
     * @return
     */
    Boolean delete(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 鏇存柊鎵€鏈夌敤鎴风殑鍔ㄦ€佷俊鎭?
     *
     * @return
     */
    void updateAll();

}
