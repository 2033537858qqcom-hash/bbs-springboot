package com.liang.bbs.rest.controller;

import com.github.pagehelper.PageInfo;
import com.liang.bbs.rest.config.login.NoNeedLogin;
import com.liang.bbs.rest.config.swagger.ApiVersion;
import com.liang.bbs.rest.config.swagger.ApiVersionConstant;
import com.liang.bbs.rest.client.UserDynamicClient;
import com.liang.bbs.user.facade.dto.InternalDynamicListRequest;
import com.liang.bbs.user.facade.dto.DynamicDTO;
import com.liang.nansheng.common.web.basic.ResponseResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 */
@Slf4j
@RestController
@RequestMapping("/bbs/dynamic/")
@Tag(name = "API")
public class DynamicController {
    @Autowired
    private UserDynamicClient userDynamicClient;

    @NoNeedLogin
    @GetMapping("getList")
@Operation(summary = "API")
    @ApiVersion(group = ApiVersionConstant.V_300)
    public ResponseResult<PageInfo<DynamicDTO>> getList(@RequestParam Long userId, @RequestParam Integer currentPage, @RequestParam Integer pageSize) {
        InternalDynamicListRequest request = new InternalDynamicListRequest();
        request.setUserId(userId);
        request.setCurrentPage(currentPage);
        request.setPageSize(pageSize);
        return ResponseResult.success(userDynamicClient.getList(request));
    }

}

