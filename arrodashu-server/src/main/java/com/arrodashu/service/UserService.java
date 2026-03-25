package com.arrodashu.service;

import com.arrodashu.dto.LoginDTO;
import com.arrodashu.dto.RegisterDTO;
import com.arrodashu.vo.LoginVO;
import com.arrodashu.vo.UserInfoVO;

/**
 * 用户服务接口
 */
public interface UserService {
    
    /**
     * 用户登录
     */
    LoginVO login(LoginDTO loginDTO);
    
    /**
     * 用户注册
     */
    void register(RegisterDTO registerDTO);
    
    /**
     * 获取用户信息
     */
    UserInfoVO getUserInfo(Long userId);
    
    /**
     * 根据用户名获取用户ID
     */
    Long getUserIdByUsername(String username);
}
