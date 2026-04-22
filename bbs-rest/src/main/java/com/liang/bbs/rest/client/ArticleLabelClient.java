package com.liang.bbs.rest.client;

import com.github.pagehelper.PageInfo;
import com.liang.bbs.article.facade.dto.InternalBinaryUploadRequest;
import com.liang.bbs.article.facade.dto.InternalLabelOperateRequest;
import com.liang.bbs.article.facade.dto.LabelDTO;
import com.liang.bbs.article.facade.dto.LabelSearchDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        contextId = "articleLabelClient",
        name = "${local.services.bbs-article.name:ns-bbs-article}"
,
        path = "/internal/article/label"
)
public interface ArticleLabelClient {

    @PostMapping("/list")
    PageInfo<LabelDTO> getList(@RequestBody LabelSearchDTO labelSearchDTO);

    @PostMapping("/create")
    Boolean create(@RequestBody InternalLabelOperateRequest request);

    @PostMapping("/upload-logo")
    String uploadLabelLogo(@RequestBody InternalBinaryUploadRequest request);

    @PostMapping("/update")
    Boolean update(@RequestBody InternalLabelOperateRequest request);

    @PostMapping("/delete/{id}")
    Boolean delete(@PathVariable("id") Integer id);
}
