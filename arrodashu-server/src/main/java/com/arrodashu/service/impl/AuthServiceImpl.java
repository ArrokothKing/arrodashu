package com.arrodashu.service.impl;

import com.arrodashu.common.exception.BusinessException;
import com.arrodashu.dto.LoginRequest;
import com.arrodashu.dto.RegisterRequest;
import com.arrodashu.entity.User;
import com.arrodashu.mapper.UserMapper;
import com.arrodashu.security.JwtUtils;
import com.arrodashu.service.AuthService;
import com.arrodashu.vo.LoginVO;
import com.arrodashu.vo.UserInfoVO;
import com.arrodashu.vo.UserVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 认证服务实现
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    
    @Override
    public LoginVO login(LoginRequest request) {
        // 查询用户
        User user = userMapper.selectOne(
            new LambdaQueryWrapper<User>()
                .eq(User::getUsername, request.getUsername())
        );
        
        if (user == null) {
            throw new BusinessException("用户名或密码错误");
        }
        
        // 验证密码
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }
        
        // 检查用户状态
        if (user.getStatus() != 1) {
            throw new BusinessException("账号已被禁用");
        }
        
        // 生成 Token
        String token = jwtUtils.generateToken(user.getId(), user.getUsername());
        
        // 组装返回数据
        LoginVO loginVO = new LoginVO();
        loginVO.setToken(token);
        loginVO.setUser(convertToUserInfoVO(user));
        
        return loginVO;
    }
    
    @Override
    public UserVO register(RegisterRequest request) {
        // 检查用户名是否已存在
        Long count = userMapper.selectCount(
            new LambdaQueryWrapper<User>()
                .eq(User::getUsername, request.getUsername())
        );
        if (count > 0) {
            throw new BusinessException("用户名已存在");
        }
        
        // 检查邮箱是否已存在
        if (request.getEmail() != null) {
            count = userMapper.selectCount(
                new LambdaQueryWrapper<User>()
                    .eq(User::getEmail, request.getEmail())
            );
            if (count > 0) {
                throw new BusinessException("邮箱已被注册");
            }
        }
        
        // 创建用户
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setNickname(request.getNickname() != null ? request.getNickname() : request.getUsername());
        user.setStatus(1);
        
        userMapper.insert(user);
        
        return convertToVO(user);
    }
    
    @Override
    public UserVO getCurrentUser(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException("用户不存在");
        }
        return convertToVO(user);
    }
    
    /**
     * 转换为 UserVO
     */
    private UserVO convertToVO(User user) {
        UserVO vo = new UserVO();
        BeanUtils.copyProperties(user, vo);
        return vo;
    }

    /**
     * 转换为 UserInfoVO
     */
    private UserInfoVO convertToUserInfoVO(User user) {
        UserInfoVO vo = new UserInfoVO();
        BeanUtils.copyProperties(user, vo);
        return vo;
    }
}
