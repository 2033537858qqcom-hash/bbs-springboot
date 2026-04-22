package com.liang.bbs.rest.client;

import com.github.pagehelper.PageInfo;
import com.liang.bbs.article.facade.dto.InternalBinaryUploadRequest;
import com.liang.bbs.article.facade.dto.InternalResourceNavigateOperateRequest;
import com.liang.bbs.article.facade.dto.ResourceNavigateDTO;
import com.liang.bbs.article.facade.dto.ResourceNavigateSearchDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(
        contextId = "articleResourceNavigateClient",
        name = "${local.services.bbs-article.name:ns-bbs-article}"
,
        path = "/internal/article/resource"
)
public interface ArticleResourceNavigateClient {

    @PostMapping("/list")
    PageInfo<ResourceNavigateDTO> getList(@RequestBody ResourceNavigateSearchDTO resourceNavigateSearchDTO);

    @PostMapping("/create")
    Boolean create(@RequestBody InternalResourceNavigateOperateRequest request);

    @PostMapping("/upload-logo")
    String uploadResourceNavigateLogo(@RequestBody InternalBinaryUploadRequest request);

    @PostMapping("/update")
    Boolean update(@RequestBody InternalResourceNavigateOperateRequest request);

    @PostMapping("/delete/{id}")
    Boolean delete(@PathVariable("id") Integer id);

    @GetMapping("/categorys")
    List<String> getCategorys();
}
