package com.arrodashu.service.impl;

import com.arrodashu.entity.Genus;
import com.arrodashu.mapper.GenusMapper;
import com.arrodashu.service.GenusService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 属服务实现类
 */
@Service
public class GenusServiceImpl extends ServiceImpl<GenusMapper, Genus> implements GenusService {
    
    @Override
    public List<Genus> getAll() {
        return this.list();
    }
    
    @Override
    public List<Genus> getByFamilyId(Long familyId) {
        LambdaQueryWrapper<Genus> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Genus::getFamilyId, familyId);
        return this.list(wrapper);
    }
}
