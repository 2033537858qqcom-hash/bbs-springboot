package com.liang.bbs.article.service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        contextId = "articleFileServiceClient",
        url = "${local.services.manage-auth.url}",  // 本地认证服务直连
        path = "/file"
)
public interface FileServiceClient {

    @PostMapping("/cut-upload")
    String fileCutUpload(@RequestParam("bytes") byte[] bytes,
                         @RequestParam("sourceFileName") String sourceFileName,
                         @RequestParam("imageType") String imageType);

    @PostMapping("/scale-upload")
    String fileScaleUpload(@RequestParam("bytes") byte[] bytes,
                           @RequestParam("sourceFileName") String sourceFileName,
                           @RequestParam("imageType") String imageType);
}
