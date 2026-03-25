package com.arrodashu.controller;

import com.arrodashu.common.result.Result;
import com.arrodashu.dto.LoginDTO;
import com.arrodashu.dto.RegisterDTO;
import com.arrodashu.service.UserService;
import com.arrodashu.vo.LoginVO;
import com.arrodashu.vo.UserInfoVO;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 */
@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {
    
    @Resource
    private UserService userService;
    
    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<LoginVO> login(@RequestBody LoginDTO loginDTO) {
        try {
            LoginVO loginVO = userService.login(loginDTO);
            return Result.success(loginVO);
        } catch (Exception e) {
            log.error("登录异常：", e);
            throw e;
        }
    }
    
    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result<Void> register(@RequestBody RegisterDTO registerDTO) {
        try {
            userService.register(registerDTO);
            return Result.success();
        } catch (Exception e) {
            log.error("注册异常：", e);
            throw e;
        }
    }
    
    /**
     * 获取当前用户信息
     */
    @GetMapping("/info")
    public Result<UserInfoVO> getUserInfo(HttpServletRequest request) {
        try {
            Long userId = (Long) request.getAttribute("userId");
            if (userId == null) {
                return Result.error(401, "未登录");
            }
            UserInfoVO userInfo = userService.getUserInfo(userId);
            return Result.success(userInfo);
        } catch (Exception e) {
            log.error("获取用户信息异常：", e);
            throw e;
        }
    }
    
    /**
     * 用户登出
     */
    @PostMapping("/logout")
    public Result<Void> logout(HttpServletRequest request) {
        try {
            Long userId = (Long) request.getAttribute("userId");
            if (userId != null) {
                log.info("用户登出：userId={}", userId);
            }
            return Result.success();
        } catch (Exception e) {
            log.error("登出异常：", e);
            throw e;
        }
    }
}
