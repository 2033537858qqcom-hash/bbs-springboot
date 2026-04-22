package com.liang.bbs.user.service.controller.internal;

import com.github.pagehelper.PageInfo;
import com.liang.bbs.user.facade.dto.DynamicDTO;
import com.liang.bbs.user.facade.dto.InternalDynamicListRequest;
import com.liang.bbs.user.facade.server.DynamicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/user/dynamic")
public class InternalDynamicController {

    @Autowired
    private DynamicService dynamicService;

    @PostMapping("/list")
    public PageInfo<DynamicDTO> getList(@RequestBody InternalDynamicListRequest request) {
        return dynamicService.getByUserId(request.getUserId(), request.getCurrentPage(), request.getPageSize());
    }
}
