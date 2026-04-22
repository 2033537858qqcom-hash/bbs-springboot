package com.liang.bbs.article.service.controller.internal;

import com.github.pagehelper.PageInfo;
import com.liang.bbs.article.facade.dto.InternalBinaryUploadRequest;
import com.liang.bbs.article.facade.dto.InternalLabelOperateRequest;
import com.liang.bbs.article.facade.dto.LabelDTO;
import com.liang.bbs.article.facade.dto.LabelSearchDTO;
import com.liang.bbs.article.facade.server.LabelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/article/label")
public class InternalLabelController {

    @Autowired
    private LabelService labelService;

    @PostMapping("/list")
    public PageInfo<LabelDTO> getList(@RequestBody LabelSearchDTO labelSearchDTO) {
        return labelService.getList(labelSearchDTO);
    }

    @PostMapping("/create")
    public Boolean create(@RequestBody InternalLabelOperateRequest request) {
        return labelService.create(request.getLabelDTO(), request.getCurrentUser());
    }

    @PostMapping("/upload-logo")
    public String uploadLabelLogo(@RequestBody InternalBinaryUploadRequest request) {
        return labelService.uploadLabelLogo(request.getBytes(), request.getSourceFileName());
    }

    @PostMapping("/update")
    public Boolean update(@RequestBody InternalLabelOperateRequest request) {
        return labelService.update(request.getLabelDTO(), request.getCurrentUser());
    }

    @PostMapping("/delete/{id}")
    public Boolean delete(@PathVariable Integer id) {
        return labelService.delete(id);
    }
}
