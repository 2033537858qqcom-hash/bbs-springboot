package com.liang.bbs.article.service.controller.internal;

import com.github.pagehelper.PageInfo;
import com.liang.bbs.article.facade.dto.InternalBinaryUploadRequest;
import com.liang.bbs.article.facade.dto.InternalResourceNavigateOperateRequest;
import com.liang.bbs.article.facade.dto.ResourceNavigateDTO;
import com.liang.bbs.article.facade.dto.ResourceNavigateSearchDTO;
import com.liang.bbs.article.facade.server.ResourceNavigateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/internal/article/resource")
public class InternalResourceNavigateController {

    @Autowired
    private ResourceNavigateService resourceNavigateService;

    @PostMapping("/list")
    public PageInfo<ResourceNavigateDTO> getList(@RequestBody ResourceNavigateSearchDTO resourceNavigateSearchDTO) {
        return resourceNavigateService.getList(resourceNavigateSearchDTO);
    }

    @PostMapping("/create")
    public Boolean create(@RequestBody InternalResourceNavigateOperateRequest request) {
        return resourceNavigateService.create(request.getResourceNavigateDTO(), request.getCurrentUser());
    }

    @PostMapping("/upload-logo")
    public String uploadResourceNavigateLogo(@RequestBody InternalBinaryUploadRequest request) {
        return resourceNavigateService.uploadResourceNavigateLogo(request.getBytes(), request.getSourceFileName());
    }

    @PostMapping("/update")
    public Boolean update(@RequestBody InternalResourceNavigateOperateRequest request) {
        return resourceNavigateService.update(request.getResourceNavigateDTO(), request.getCurrentUser());
    }

    @PostMapping("/delete/{id}")
    public Boolean delete(@PathVariable Integer id) {
        return resourceNavigateService.delete(id);
    }

    @GetMapping("/categorys")
    public List<String> getCategorys() {
        return resourceNavigateService.getCategorys();
    }
}
