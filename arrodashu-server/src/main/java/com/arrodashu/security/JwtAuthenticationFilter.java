package com.arrodashu.security;

import com.arrodashu.security.JwtUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;

/**
 * JWT认证过滤器
 */
@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    @Resource
    private JwtUtils jwtUtils;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            // 获取Token
            String token = getTokenFromRequest(request);

            // 检查是否存在token且还未被认证
            if (StringUtils.hasText(token) && SecurityContextHolder.getContext().getAuthentication() == null) {
                // 验证Token
                if (jwtUtils.validateToken(token)) {
                    // 获取用户ID
                    Long userId = jwtUtils.getUserIdFromToken(token);
                    String username = jwtUtils.getUsernameFromToken(token);

                    if (userId != null && StringUtils.hasText(username)) {
                        // 创建认证对象
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(
                                        userId,
                                        null,
                                        new ArrayList<>()
                                );
                        authentication.setDetails(username);

                        // 设置安全上下文
                        SecurityContextHolder.getContext().setAuthentication(authentication);

                        // 将用户ID存入request属性
                        request.setAttribute("userId", userId);
                        request.setAttribute("username", username);
                    }
                }
            }
        } catch (Exception e) {
            log.error("JWT认证失败：", e);
            // 发生异常时不中断请求，让后续的错误处理机制处理
        }

        filterChain.doFilter(request, response);
    }
    
    /**
     * 从请求中获取Token
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
