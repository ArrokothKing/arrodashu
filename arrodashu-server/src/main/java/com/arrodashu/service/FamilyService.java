package com.arrodashu.service;

import com.arrodashu.entity.Family;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * 科服务接口
 */
public interface FamilyService extends IService<Family> {
    
    /**
     * 获取所有科列表
     */
    List<Family> getAll();
}
