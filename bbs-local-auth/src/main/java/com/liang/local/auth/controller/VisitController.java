package com.liang.local.auth.controller;

import com.liang.manage.auth.facade.dto.visit.VisitDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 本地访问记录Controller
 */
@Slf4j
@RestController
@RequestMapping("/visit")
public class VisitController {

    private AtomicLong visitCounter = new AtomicLong(0);

    /**
     * 创建访问记录
     */
    @PostMapping("/create")
    public Boolean create(@RequestBody VisitDTO visitDTO) {
        visitCounter.incrementAndGet();
        log.info("访问记录: IP={}, OS={}", visitDTO.getIp(), visitDTO.getOs());
        return true;
    }

    /**
     * 获取总访问数
     */
    @GetMapping("/total")
    public Long getTotal() {
        return visitCounter.get();
    }
}
