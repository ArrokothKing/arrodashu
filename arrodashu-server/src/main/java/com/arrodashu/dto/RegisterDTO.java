package com.arrodashu.dto;

import lombok.Data;

/**
 * 注册请求DTO
 */
@Data
public class RegisterDTO {
    private String username;
    private String password;
    private String email;
    private String nickname;
}
