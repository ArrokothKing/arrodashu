package com.arrodashu.service.impl;

import com.arrodashu.entity.Family;
import com.arrodashu.mapper.FamilyMapper;
import com.arrodashu.service.FamilyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 科服务实现类
 */
@Service
public class FamilyServiceImpl extends ServiceImpl<FamilyMapper, Family> implements FamilyService {
    
    @Override
    public List<Family> getAll() {
        return this.list();
    }
}
