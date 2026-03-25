package com.arrodashu.service;

import com.arrodashu.dto.LoginRequest;
import com.arrodashu.dto.RegisterRequest;
import com.arrodashu.vo.LoginVO;
import com.arrodashu.vo.UserVO;

/**
 * 认证服务接口
 */
public interface AuthService {
    
    /**
     * 用户登录
     */
    LoginVO login(LoginRequest request);
    
    /**
     * 用户注册
     */
    UserVO register(RegisterRequest request);
    
    /**
     * 获取当前用户信息
     */
    UserVO getCurrentUser(Long userId);
}
