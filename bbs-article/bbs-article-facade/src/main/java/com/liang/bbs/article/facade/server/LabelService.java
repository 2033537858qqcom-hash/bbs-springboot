package com.liang.bbs.article.facade.server;

import com.github.pagehelper.PageInfo;
import com.liang.bbs.article.facade.dto.LabelDTO;
import com.liang.bbs.article.facade.dto.LabelSearchDTO;
import com.liang.nansheng.common.auth.UserSsoDTO;

import java.util.List;

/**
 */
public interface LabelService {
    /**
     * 鑾峰彇鏍囩
     *
     * @param labelSearchDTO
     * @return
     */
    PageInfo<LabelDTO> getList(LabelSearchDTO labelSearchDTO);

    /**
     * 閫氳繃鏍囩id闆嗗悎鑾峰彇鏍囩淇℃伅
     *
     * @param ids
     * @return
     */
    List<LabelDTO> getByIds(List<Integer> ids);

    /**
     * 鏂板鏍囩
     *
     * @param labelDTO
     * @param currentUser
     * @return
     */
    Boolean create(LabelDTO labelDTO, UserSsoDTO currentUser);

    /**
     * 涓婁紶鏍囩logo
     *
     * @param bytes
     * @param sourceFileName
     * @return
     */
    String uploadLabelLogo(byte[] bytes, String sourceFileName);

    /**
     * 鏇存柊鏍囩
     *
     * @param labelDTO
     * @param currentUser
     * @return
     */
    Boolean update(LabelDTO labelDTO, UserSsoDTO currentUser);

    /**
     * 鍒犻櫎鏍囩
     *
     * @param id
     * @return
     */
    Boolean delete(Integer id);
}
