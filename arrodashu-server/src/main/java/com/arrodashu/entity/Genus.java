package com.arrodashu.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 属实体
 */
@Data
@TableName("tree_genus")
public class Genus {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String name;
    
    private String latinName;
    
    private Long familyId;
    
    private String description;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    // 非数据库字段
    @TableField(exist = false)
    private String familyName;
}
