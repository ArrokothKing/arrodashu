package com.arrodashu.service;

import com.arrodashu.entity.Genus;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 属服务接口
 */
public interface GenusService extends IService<Genus> {
    
    /**
     * 获取所有属列表
     */
    List<Genus> getAll();
    
    /**
     * 根据科ID获取属列表
     */
    List<Genus> getByFamilyId(Long familyId);
}
