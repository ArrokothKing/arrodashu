package com.arrodashu.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 科实体
 */
@Data
@TableName("tree_family")
public class Family {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String name;
    
    private String latinName;
    
    private String description;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
