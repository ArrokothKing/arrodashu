package com.arrodashu.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 品种实体
 */
@Data
@TableName("tree_species")
public class Species {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String name;
    
    private String latinName;
    
    private String alias;
    
    private Long familyId;
    
    private Long genusId;
    
    // 形态特征
    private Integer treeType;
    
    private Integer heightMin;
    
    private Integer heightMax;
    
    private String crownShape;
    
    private String barkDesc;
    
    private String leafDesc;
    
    private String flowerDesc;
    
    private String fruitDesc;
    
    // 生长习性
    private String lightPreference;
    
    private String waterPreference;
    
    private String soilPreference;
    
    private String hardinessZone;
    
    private String growthRate;
    
    // 园林用途
    private String landscapeUse;
    
    // 其他
    private String description;
    
    private String origin;
    
    private Long createBy;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    private Integer status;
    
    // 非数据库字段
    @TableField(exist = false)
    private String familyName;
    
    @TableField(exist = false)
    private String genusName;
    
    @TableField(exist = false)
    private List<String> images;
    
    @TableField(exist = false)
    private List<Long> tagIds;
}
