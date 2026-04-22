package com.liang.bbs.rest.controller;

import com.liang.bbs.article.facade.dto.SlideshowDTO;
import com.liang.bbs.rest.client.ArticleSlideshowClient;
import com.liang.bbs.rest.config.login.NoNeedLogin;
import com.liang.bbs.rest.config.swagger.ApiVersion;
import com.liang.bbs.rest.config.swagger.ApiVersionConstant;
import com.liang.nansheng.common.web.basic.ResponseResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 */
@Slf4j
@RestController
@RequestMapping("/bbs/carousel/")
@Tag(name = "API")
public class SlideshowController {
    @Autowired
    private ArticleSlideshowClient articleSlideshowClient;

    @NoNeedLogin
    @GetMapping("getList")
@Operation(summary = "API")
    @ApiVersion(group = ApiVersionConstant.V_300)
    public ResponseResult<List<SlideshowDTO>> getList() {
        return ResponseResult.success(articleSlideshowClient.getList());
    }

}

