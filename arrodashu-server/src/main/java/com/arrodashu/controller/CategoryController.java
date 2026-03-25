package com.arrodashu.controller;

import com.arrodashu.common.result.Result;
import com.arrodashu.entity.Family;
import com.arrodashu.entity.Genus;
import com.arrodashu.service.FamilyService;
import com.arrodashu.service.GenusService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 分类控制器
 */
@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {
    
    @Resource
    private FamilyService familyService;
    
    @Resource
    private GenusService genusService;
    
    /**
     * 获取所有科
     */
    @GetMapping("/families")
    public Result<List<Family>> getFamilies() {
        try {
            List<Family> list = familyService.getAll();
            return Result.success(list);
        } catch (Exception e) {
            log.error("获取科列表异常：", e);
            throw e;
        }
    }
    
    /**
     * 获取所有属
     */
    @GetMapping("/genera")
    public Result<List<Genus>> getGenera() {
        try {
            List<Genus> list = genusService.getAll();
            return Result.success(list);
        } catch (Exception e) {
            log.error("获取属列表异常：", e);
            throw e;
        }
    }
    
    /**
     * 根据科ID获取属列表
     */
    @GetMapping("/family/{familyId}/genera")
    public Result<List<Genus>> getGeneraByFamily(@PathVariable Long familyId) {
        try {
            List<Genus> list = genusService.getByFamilyId(familyId);
            return Result.success(list);
        } catch (Exception e) {
            log.error("获取属列表异常：", e);
            throw e;
        }
    }
}
