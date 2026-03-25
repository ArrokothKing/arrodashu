package com.arrodashu.service.impl;

import com.arrodashu.common.exception.BusinessException;
import com.arrodashu.entity.Species;
import com.arrodashu.mapper.FamilyMapper;
import com.arrodashu.mapper.GenusMapper;
import com.arrodashu.mapper.SpeciesMapper;
import com.arrodashu.service.SpeciesService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.Resource;
import java.util.List;

/**
 * 品种服务实现类
 */
@Slf4j
@Service
public class SpeciesServiceImpl extends ServiceImpl<SpeciesMapper, Species> implements SpeciesService {
    
    @Resource
    private FamilyMapper familyMapper;
    
    @Resource
    private GenusMapper genusMapper;
    
    @Override
    public Page<Species> pageQuery(Integer page, Integer size, String keyword, Long familyId, Long genusId) {
        Page<Species> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Species> wrapper = new LambdaQueryWrapper<>();
        
        // 只查询正常状态的
        wrapper.eq(Species::getStatus, 1);
        
        // 关键词搜索
        if (keyword != null && !keyword.trim().isEmpty()) {
            wrapper.and(w -> w.like(Species::getName, keyword)
                    .or()
                    .like(Species::getLatinName, keyword));
        }
        
        // 科筛选
        if (familyId != null) {
            wrapper.eq(Species::getFamilyId, familyId);
        }
        
        // 属筛选
        if (genusId != null) {
            wrapper.eq(Species::getGenusId, genusId);
        }
        
        // 按创建时间倒序
        wrapper.orderByDesc(Species::getCreateTime);
        
        Page<Species> result = this.page(pageParam, wrapper);
        
        // 填充科属名称
        result.getRecords().forEach(this::fillNames);
        
        return result;
    }
    
    @Override
    public Species getDetail(Long id) {
        Species species = this.getById(id);
        if (species == null || species.getStatus() != 1) {
            throw new BusinessException(404, "品种不存在");
        }
        fillNames(species);
        return species;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void add(Species species, List<String> images, List<Long> tagIds) {
        // 参数校验
        if (species.getName() == null || species.getName().trim().isEmpty()) {
            throw new BusinessException(400, "品种名称不能为空");
        }
        if (species.getFamilyId() == null) {
            throw new BusinessException(400, "所属科不能为空");
        }
        if (species.getGenusId() == null) {
            throw new BusinessException(400, "所属属不能为空");
        }
        
        // 检查是否已存在
        LambdaQueryWrapper<Species> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Species::getName, species.getName());
        long count = this.count(wrapper);
        if (count > 0) {
            throw new BusinessException(400, "该品种名称已存在");
        }
        
        species.setStatus(1);
        this.save(species);
        
        log.info("添加品种成功：{}", species.getName());
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Species species, List<String> images, List<Long> tagIds) {
        if (species.getId() == null) {
            throw new BusinessException(400, "品种ID不能为空");
        }
        
        Species exist = this.getById(species.getId());
        if (exist == null || exist.getStatus() != 1) {
            throw new BusinessException(404, "品种不存在");
        }
        
        this.updateById(species);
        log.info("更新品种成功：id={}", species.getId());
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        Species species = this.getById(id);
        if (species == null || species.getStatus() != 1) {
            throw new BusinessException(404, "品种不存在");
        }
        
        // 逻辑删除
        species.setStatus(0);
        this.updateById(species);
        
        log.info("删除品种成功：id={}", id);
    }
    
    /**
     * 填充科属名称
     */
    private void fillNames(Species species) {
        if (species.getFamilyId() != null) {
            species.setFamilyName(familyMapper.selectById(species.getFamilyId()).getName());
        }
        if (species.getGenusId() != null) {
            species.setGenusName(genusMapper.selectById(species.getGenusId()).getName());
        }
    }
}
