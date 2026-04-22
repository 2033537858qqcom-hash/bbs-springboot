package com.liang.bbs.article.facade.server;

import com.github.pagehelper.PageInfo;
import com.liang.bbs.article.facade.dto.ResourceNavigateDTO;
import com.liang.bbs.article.facade.dto.ResourceNavigateSearchDTO;
import com.liang.nansheng.common.auth.UserSsoDTO;

import java.util.List;

/**
 */
public interface ResourceNavigateService {
    /**
     * 鑾峰彇璧勬簮瀵艰埅
     *
     * @param resourceNavigateSearchDTO
     * @return
     */
    PageInfo<ResourceNavigateDTO> getList(ResourceNavigateSearchDTO resourceNavigateSearchDTO);

    /**
     * 鏂板璧勬簮瀵艰埅
     *
     * @param resourceNavigateDTO
     * @param currentUser
     * @return
     */
    Boolean create(ResourceNavigateDTO resourceNavigateDTO, UserSsoDTO currentUser);

    /**
     * 涓婁紶璧勬簮瀵艰埅logo
     *
     * @param bytes
     * @param sourceFileName
     * @return
     */
    String uploadResourceNavigateLogo(byte[] bytes, String sourceFileName);

    /**
     * 鏇存柊璧勬簮瀵艰埅
     *
     * @param resourceNavigateDTO
     * @param currentUser
     * @return
     */
    Boolean update(ResourceNavigateDTO resourceNavigateDTO, UserSsoDTO currentUser);

    /**
     * 鍒犻櫎璧勬簮瀵艰埅
     *
     * @param id
     * @return
     */
    Boolean delete(Integer id);

    /**
     * 鑾峰彇璧勬簮瀵艰埅鎵€鏈夌被鍒?
     *
     * @return
     */
    List<String> getCategorys();
}
