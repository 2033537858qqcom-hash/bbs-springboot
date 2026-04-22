package com.liang.bbs.rest.controller;

import com.github.pagehelper.PageInfo;
import com.liang.bbs.article.facade.dto.InternalBinaryUploadRequest;
import com.liang.bbs.article.facade.dto.InternalResourceNavigateOperateRequest;
import com.liang.bbs.article.facade.dto.ResourceNavigateDTO;
import com.liang.bbs.article.facade.dto.ResourceNavigateSearchDTO;
import com.liang.bbs.rest.config.login.NoNeedLogin;
import com.liang.bbs.rest.config.swagger.ApiVersion;
import com.liang.bbs.rest.config.swagger.ApiVersionConstant;
import com.liang.bbs.rest.client.ArticleResourceNavigateClient;
import com.liang.bbs.rest.utils.FileLengthUtils;
import com.liang.nansheng.common.auth.UserContextUtils;
import com.liang.nansheng.common.auth.UserSsoDTO;
import com.liang.nansheng.common.enums.ResponseCode;
import com.liang.nansheng.common.utils.CommonUtils;
import com.liang.nansheng.common.web.basic.ResponseResult;
import com.liang.nansheng.common.web.exception.BusinessException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 */
@Slf4j
@RestController
@RequestMapping("/bbs/resource/")
@Tag(name = "API")
public class ResourceController {
    @Autowired
    private ArticleResourceNavigateClient articleResourceNavigateClient;

    @Autowired
    private FileLengthUtils fileLengthUtils;

    @PostMapping("create")
@Operation(summary = "API")
    @ApiVersion(group = ApiVersionConstant.V_300)
    public ResponseResult<Boolean> create(@RequestBody ResourceNavigateDTO resourceNavigateDTO) {
        UserSsoDTO currentUser = UserContextUtils.currentUser();
        InternalResourceNavigateOperateRequest request = new InternalResourceNavigateOperateRequest();
        request.setResourceNavigateDTO(resourceNavigateDTO);
        request.setCurrentUser(currentUser);
        return ResponseResult.success(articleResourceNavigateClient.create(request));
    }

    @PostMapping("/uploadResourceLogo")
@Operation(summary = "API")
    @ApiVersion(group = ApiVersionConstant.V_300)
    public ResponseResult<String> uploadResourceLogo(@RequestParam(value = "logo", required = false) MultipartFile logo) throws IOException {
        if (fileLengthUtils.isFileNotTooBig(logo.getBytes())) {
            InternalBinaryUploadRequest request = new InternalBinaryUploadRequest();
            request.setBytes(logo.getBytes());
            request.setSourceFileName(logo.getOriginalFilename());
            return ResponseResult.success(articleResourceNavigateClient.uploadResourceNavigateLogo(request));
        } else {
            throw BusinessException.build(ResponseCode.EXCEED_THE_MAX, "鐠囪渹绗傛导鐘辩瑝鐡掑懓绻?" +
                    CommonUtils.byteConversion(fileLengthUtils.getFileMaxLength()) + " 閻ㄥ嫬娴橀悧?");
        }
    }

    @PostMapping("update")
@Operation(summary = "API")
    @ApiVersion(group = ApiVersionConstant.V_300)
    public ResponseResult<Boolean> update(@RequestBody ResourceNavigateDTO resourceNavigateDTO) {
        UserSsoDTO currentUser = UserContextUtils.currentUser();
        InternalResourceNavigateOperateRequest request = new InternalResourceNavigateOperateRequest();
        request.setResourceNavigateDTO(resourceNavigateDTO);
        request.setCurrentUser(currentUser);
        return ResponseResult.success(articleResourceNavigateClient.update(request));
    }

    @NoNeedLogin
    @GetMapping("getList")
@Operation(summary = "API")
    @ApiVersion(group = ApiVersionConstant.V_300)
    public ResponseResult<PageInfo<ResourceNavigateDTO>> getList(ResourceNavigateSearchDTO resourceNavigateSearchDTO) {
        return ResponseResult.success(articleResourceNavigateClient.getList(resourceNavigateSearchDTO));
    }

    @NoNeedLogin
    @GetMapping("getCategorys")
@Operation(summary = "API")
    @ApiVersion(group = ApiVersionConstant.V_300)
    public ResponseResult<List<String>> getCategorys() {
        return ResponseResult.success(articleResourceNavigateClient.getCategorys());
    }

    @PostMapping("delete/{id}")
@Operation(summary = "API")
    @ApiVersion(group = ApiVersionConstant.V_300)
    public ResponseResult<Boolean> delete(@PathVariable Integer id) {
        return ResponseResult.success(articleResourceNavigateClient.delete(id));
    }

}


