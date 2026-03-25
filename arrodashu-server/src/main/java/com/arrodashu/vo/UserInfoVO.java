package com.arrodashu.vo;

import lombok.Data;

/**
 * 用户信息VO
 */
@Data
public class UserInfoVO {
    private Long id;
    private String username;
    private String nickname;
    private String avatar;
    private String email;
}
