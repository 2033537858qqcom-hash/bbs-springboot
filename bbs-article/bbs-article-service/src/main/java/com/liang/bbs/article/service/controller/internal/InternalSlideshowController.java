package com.liang.bbs.article.service.controller.internal;

import com.liang.bbs.article.facade.dto.SlideshowDTO;
import com.liang.bbs.article.facade.server.SlideshowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/internal/article/slideshow")
public class InternalSlideshowController {

    @Autowired
    private SlideshowService slideshowService;

    @GetMapping("/list")
    public List<SlideshowDTO> getList() {
        return slideshowService.getList();
    }
}
