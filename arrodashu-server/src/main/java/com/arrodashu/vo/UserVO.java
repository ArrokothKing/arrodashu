package com.arrodashu.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户信息视图
 */
@Data
public class UserVO {
    
    private Long id;
    private String username;
    private String email;
    private String phone;
    private String nickname;
    private String avatar;
    private Integer status;
    private LocalDateTime createTime;
}
