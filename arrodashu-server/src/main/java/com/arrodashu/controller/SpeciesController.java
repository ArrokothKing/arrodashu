package com.arrodashu.controller;

import com.arrodashu.common.result.Result;
import com.arrodashu.entity.Species;
import com.arrodashu.service.SpeciesService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 品种控制器
 */
@Slf4j
@RestController
@RequestMapping("/species")
public class SpeciesController {
    
    @Resource
    private SpeciesService speciesService;
    
    /**
     * 分页查询品种列表
     */
    @GetMapping
    public Result<Page<Species>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "12") Integer size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long familyId,
            @RequestParam(required = false) Long genusId
    ) {
        try {
            Page<Species> result = speciesService.pageQuery(page, size, keyword, familyId, genusId);
            return Result.success(result);
        } catch (Exception e) {
            log.error("查询品种列表异常：", e);
            throw e;
        }
    }
    
    /**
     * 获取品种详情
     */
    @GetMapping("/{id}")
    public Result<Species> detail(@PathVariable Long id) {
        try {
            Species species = speciesService.getDetail(id);
            return Result.success(species);
        } catch (Exception e) {
            log.error("获取品种详情异常：", e);
            throw e;
        }
    }
    
    /**
     * 添加品种
     */
    @PostMapping
    public Result<Void> add(@RequestBody Species species,
                            @RequestParam(required = false) List<String> images,
                            @RequestParam(required = false) List<Long> tagIds,
                            HttpServletRequest request) {
        try {
            Long userId = (Long) request.getAttribute("userId");
            species.setCreateBy(userId);
            speciesService.add(species, images, tagIds);
            return Result.success();
        } catch (Exception e) {
            log.error("添加品种异常：", e);
            throw e;
        }
    }
    
    /**
     * 更新品种
     */
    @PutMapping("/{id}")
    public Result<Void> update(@PathVariable Long id, @RequestBody Species species) {
        try {
            species.setId(id);
            speciesService.update(species, null, null);
            return Result.success();
        } catch (Exception e) {
            log.error("更新品种异常：", e);
            throw e;
        }
    }
    
    /**
     * 删除品种
     */
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        try {
            speciesService.delete(id);
            return Result.success();
        } catch (Exception e) {
            log.error("删除品种异常：", e);
            throw e;
        }
    }
}
