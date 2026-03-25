package com.arrodashu.service.impl;

import com.arrodashu.common.exception.BusinessException;
import com.arrodashu.dto.LoginDTO;
import com.arrodashu.dto.RegisterDTO;
import com.arrodashu.entity.User;
import com.arrodashu.mapper.UserMapper;
import com.arrodashu.service.UserService;
import com.arrodashu.utils.JwtUtil;
import com.arrodashu.vo.LoginVO;
import com.arrodashu.vo.UserInfoVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;

/**
 * 用户服务实现类
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    
    @Resource
    private UserMapper userMapper;
    
    @Resource
    private JwtUtil jwtUtil;
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    @Override
    public LoginVO login(LoginDTO loginDTO) {
        // 参数校验
        if (loginDTO.getUsername() == null || loginDTO.getUsername().trim().isEmpty()) {
            throw new BusinessException(400, "用户名不能为空");
        }
        if (loginDTO.getPassword() == null || loginDTO.getPassword().isEmpty()) {
            throw new BusinessException(400, "密码不能为空");
        }
        
        // 查询用户
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, loginDTO.getUsername());
        User user = userMapper.selectOne(wrapper);
        
        if (user == null) {
            throw new BusinessException(401, "用户名或密码错误");
        }
        
        // 验证密码
        if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new BusinessException(401, "用户名或密码错误");
        }
        
        // 检查用户状态
        if (user.getStatus() != 1) {
            throw new BusinessException(403, "账号已被禁用");
        }
        
        // 生成Token
        String token = jwtUtil.generateToken(user.getId(), user.getUsername());
        
        // 构建返回结果
        LoginVO loginVO = new LoginVO();
        loginVO.setToken(token);
        
        UserInfoVO userInfoVO = new UserInfoVO();
        userInfoVO.setId(user.getId());
        userInfoVO.setUsername(user.getUsername());
        userInfoVO.setNickname(user.getNickname());
        userInfoVO.setAvatar(user.getAvatar());
        userInfoVO.setEmail(user.getEmail());
        loginVO.setUser(userInfoVO);
        
        log.info("用户登录成功：{}", user.getUsername());
        return loginVO;
    }
    
    @Override
    public void register(RegisterDTO registerDTO) {
        // 参数校验
        if (registerDTO.getUsername() == null || registerDTO.getUsername().trim().isEmpty()) {
            throw new BusinessException(400, "用户名不能为空");
        }
        if (registerDTO.getPassword() == null || registerDTO.getPassword().length() < 6) {
            throw new BusinessException(400, "密码长度不能少于6位");
        }
        
        // 检查用户名是否已存在
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, registerDTO.getUsername());
        Long count = userMapper.selectCount(wrapper);
        if (count > 0) {
            throw new BusinessException(400, "用户名已存在");
        }
        
        // 检查邮箱是否已存在
        if (registerDTO.getEmail() != null && !registerDTO.getEmail().isEmpty()) {
            wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(User::getEmail, registerDTO.getEmail());
            count = userMapper.selectCount(wrapper);
            if (count > 0) {
                throw new BusinessException(400, "邮箱已被注册");
            }
        }
        
        // 创建用户
        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setEmail(registerDTO.getEmail());
        user.setNickname(registerDTO.getNickname() != null ? registerDTO.getNickname() : registerDTO.getUsername());
        user.setStatus(1);
        
        userMapper.insert(user);
        log.info("用户注册成功：{}", user.getUsername());
    }
    
    @Override
    public UserInfoVO getUserInfo(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(404, "用户不存在");
        }
        
        UserInfoVO userInfoVO = new UserInfoVO();
        userInfoVO.setId(user.getId());
        userInfoVO.setUsername(user.getUsername());
        userInfoVO.setNickname(user.getNickname());
        userInfoVO.setAvatar(user.getAvatar());
        userInfoVO.setEmail(user.getEmail());
        return userInfoVO;
    }
    
    @Override
    public Long getUserIdByUsername(String username) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        User user = userMapper.selectOne(wrapper);
        return user != null ? user.getId() : null;
    }
}
