package com.arrodashu.service;

import com.arrodashu.entity.Species;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 品种服务接口
 */
public interface SpeciesService extends IService<Species> {
    
    /**
     * 分页查询品种列表
     */
    Page<Species> pageQuery(Integer page, Integer size, String keyword, Long familyId, Long genusId);
    
    /**
     * 获取品种详情
     */
    Species getDetail(Long id);
    
    /**
     * 添加品种
     */
    void add(Species species, List<String> images, List<Long> tagIds);
    
    /**
     * 更新品种
     */
    void update(Species species, List<String> images, List<Long> tagIds);
    
    /**
     * 删除品种
     */
    void delete(Long id);
}
