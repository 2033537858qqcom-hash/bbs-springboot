package com.liang.bbs.article.facade.server;

import com.liang.bbs.article.facade.dto.SlideshowDTO;

import java.util.List;

/**
 */
public interface SlideshowService {

    /**
     * 鑾峰彇杞挱鍥句俊鎭?
     *
     * @return
     */
    List<SlideshowDTO> getList();

}
