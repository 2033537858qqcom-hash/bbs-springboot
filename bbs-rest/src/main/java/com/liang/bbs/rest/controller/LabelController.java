package com.liang.bbs.rest.controller;

import com.github.pagehelper.PageInfo;
import com.liang.bbs.article.facade.dto.InternalBinaryUploadRequest;
import com.liang.bbs.article.facade.dto.InternalLabelOperateRequest;
import com.liang.bbs.article.facade.dto.LabelDTO;
import com.liang.bbs.article.facade.dto.LabelSearchDTO;
import com.liang.bbs.rest.config.login.NoNeedLogin;
import com.liang.bbs.rest.config.swagger.ApiVersion;
import com.liang.bbs.rest.config.swagger.ApiVersionConstant;
import com.liang.bbs.rest.client.ArticleLabelClient;
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

/**
 */
@Slf4j
@RestController
@RequestMapping("/bbs/label/")
@Tag(name = "閺嶅洨顒烽幒銉ュ經")
public class LabelController {
    @Autowired
    private ArticleLabelClient articleLabelClient;

    @Autowired
    private FileLengthUtils fileLengthUtils;

    @PostMapping("create")
    @Operation(summary = "閺傛澘顤冮弽鍥╊劮")
    @ApiVersion(group = ApiVersionConstant.V_300)
    public ResponseResult<Boolean> create(@RequestBody LabelDTO labelDTO) {
        UserSsoDTO currentUser = UserContextUtils.currentUser();
        InternalLabelOperateRequest request = new InternalLabelOperateRequest();
        request.setLabelDTO(labelDTO);
        request.setCurrentUser(currentUser);
        return ResponseResult.success(articleLabelClient.create(request));
    }

    @PostMapping("/uploadLabelLogo")
    @Operation(summary = "娑撳﹣绱堕弽鍥╊劮logo")
    @ApiVersion(group = ApiVersionConstant.V_300)
    public ResponseResult<String> uploadLabelLogo(@RequestParam(value = "logo", required = false) MultipartFile logo) throws IOException {
        if (fileLengthUtils.isFileNotTooBig(logo.getBytes())) {
            InternalBinaryUploadRequest request = new InternalBinaryUploadRequest();
            request.setBytes(logo.getBytes());
            request.setSourceFileName(logo.getOriginalFilename());
            return ResponseResult.success(articleLabelClient.uploadLabelLogo(request));
        } else {
            throw BusinessException.build(ResponseCode.EXCEED_THE_MAX, "鐠囪渹绗傛导鐘辩瑝鐡掑懓绻?" +
                    CommonUtils.byteConversion(fileLengthUtils.getFileMaxLength()) + " 閻ㄥ嫬娴橀悧?");
        }
    }

    @PostMapping("update")
    @Operation(summary = "閺囧瓨鏌婇弽鍥╊劮")
    @ApiVersion(group = ApiVersionConstant.V_300)
    public ResponseResult<Boolean> update(@RequestBody LabelDTO labelDTO) {
        UserSsoDTO currentUser = UserContextUtils.currentUser();
        InternalLabelOperateRequest request = new InternalLabelOperateRequest();
        request.setLabelDTO(labelDTO);
        request.setCurrentUser(currentUser);
        return ResponseResult.success(articleLabelClient.update(request));
    }

    @NoNeedLogin
    @GetMapping("getList")
    @Operation(summary = "閼惧嘲褰囬弽鍥╊劮")
    @ApiVersion(group = ApiVersionConstant.V_300)
    public ResponseResult<PageInfo<LabelDTO>> getList(LabelSearchDTO labelSearchDTO) {
        return ResponseResult.success(articleLabelClient.getList(labelSearchDTO));
    }

    @PostMapping("delete/{id}")
    @Operation(summary = "閸掔娀娅庨弽鍥╊劮")
    @ApiVersion(group = ApiVersionConstant.V_300)
    public ResponseResult<Boolean> delete(@PathVariable Integer id) {
        return ResponseResult.success(articleLabelClient.delete(id));
    }

}


