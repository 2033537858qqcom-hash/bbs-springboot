package com.liang.manage.auth.service.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/file")
public class FileController {
    private static final String DEFAULT_BASE_URL = "http://127.0.0.1:7014";
    private final String baseUrl;

    public FileController(@Value("${local.manage-auth.base-url:" + DEFAULT_BASE_URL + "}") String baseUrl) {
        this.baseUrl = StringUtils.removeEnd(baseUrl, "/");
    }

    @PostMapping("/cut-upload")
    public String fileCutUpload(@RequestParam("bytes") byte[] bytes,
                                @RequestParam("sourceFileName") String sourceFileName,
                                @RequestParam("imageType") String imageType) {
        return buildMockUrl(sourceFileName, imageType);
    }

    @PostMapping("/scale-upload")
    public String fileScaleUpload(@RequestParam("bytes") byte[] bytes,
                                  @RequestParam("sourceFileName") String sourceFileName,
                                  @RequestParam("imageType") String imageType) {
        return buildMockUrl(sourceFileName, imageType);
    }

    private String buildMockUrl(String sourceFileName, String imageType) {
        String ext = ".png";
        if (StringUtils.isNotBlank(sourceFileName) && sourceFileName.contains(".")) {
            ext = sourceFileName.substring(sourceFileName.lastIndexOf("."));
        }
        return baseUrl + "/mock-upload/" + imageType + "/" + System.currentTimeMillis() + ext;
    }
}
