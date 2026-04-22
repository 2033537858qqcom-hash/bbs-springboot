package com.liang.bbs.rest.client;

import com.liang.bbs.article.facade.dto.SlideshowDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(
        contextId = "articleSlideshowClient",
        name = "${local.services.bbs-article.name:ns-bbs-article}"
,
        path = "/internal/article/slideshow"
)
public interface ArticleSlideshowClient {

    @GetMapping("/list")
    List<SlideshowDTO> getList();
}
