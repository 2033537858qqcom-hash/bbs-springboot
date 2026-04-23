package com.liang.manage.auth.service.controller;

import com.liang.manage.auth.facade.dto.visit.VisitDTO;
import com.liang.manage.auth.service.store.InMemoryManageAuthStore;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/visit")
public class VisitController {

    private final InMemoryManageAuthStore store;

    public VisitController(InMemoryManageAuthStore store) {
        this.store = store;
    }

    @PostMapping("/create")
    public Boolean create(@RequestBody VisitDTO visitDTO) {
        if (visitDTO == null) {
            return false;
        }
        if (visitDTO.getCreateTime() == null) {
            visitDTO.setCreateTime(LocalDateTime.now());
        }
        store.addVisit(visitDTO);
        return true;
    }

    @GetMapping("/total")
    public Long getTotal() {
        return store.visitTotal();
    }
}
