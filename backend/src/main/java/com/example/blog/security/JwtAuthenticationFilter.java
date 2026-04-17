package com.example.blog.security;

import com.example.blog.model.User;
import com.example.blog.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String path = request.getRequestURI();
        String method = request.getMethod();
        
        // 只跳过公开接口的 GET 请求的 JWT 验证
        // POST/PUT/DELETE 等写操作必须验证 JWT
        if (("GET".equals(method) && (path.startsWith("/api/articles") || path.startsWith("/api/tags"))) || 
            path.startsWith("/api/auth/login") ||
            path.startsWith("/api/auth/register") ||
            path.equals("/api/auth/status")) {
            // 对于公开 GET 请求，仍然尝试解析 token（如果有的话）
            String header = request.getHeader("Authorization");
            if (header != null && header.startsWith("Bearer ")) {
                try {
                    String token = header.substring(7);
                    String email = jwtUtil.getEmailFromToken(token);
                    if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                        User user = userService.getByEmail(email);
                        if (user != null && jwtUtil.validateToken(token)) {
                            java.util.List<org.springframework.security.core.GrantedAuthority> authorities = new java.util.ArrayList<>();
                            if (user.isAdmin()) {
                                authorities.add(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_ADMIN"));
                            }
                            authorities.add(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_USER"));
                            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                    user, null, authorities);
                            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                            request.setAttribute("user", user);
                        }
                    }
                } catch (Exception e) {
                    logger.warn("Optional JWT parsing failed for public endpoint: " + e.getMessage());
                }
            }
            chain.doFilter(request, response);
            return;
        }
        
        String header = request.getHeader("Authorization");
        String token = null;
        String email = null;

        try {
            if (header != null && header.startsWith("Bearer ")) {
                token = header.substring(7);
                email = jwtUtil.getEmailFromToken(token);
            }

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                User user = userService.getByEmail(email);
                if (user != null && jwtUtil.validateToken(token)) {
                    // 创建权限列表
                    java.util.List<org.springframework.security.core.GrantedAuthority> authorities = new java.util.ArrayList<>();
                    if (user.isAdmin()) {
                        authorities.add(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_ADMIN"));
                    }
                    authorities.add(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_USER"));
                    
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            user, null, authorities);
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    // 将用户信息设置到请求属性中
                    request.setAttribute("user", user);
                }
            }
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            logger.warn("JWT token expired: " + e.getMessage());
            request.setAttribute("expired", e.getMessage());
        } catch (io.jsonwebtoken.MalformedJwtException e) {
            logger.warn("Invalid JWT token: " + e.getMessage());
            request.setAttribute("invalid", e.getMessage());
        } catch (io.jsonwebtoken.security.SignatureException e) {
            logger.warn("Invalid JWT signature: " + e.getMessage());
            request.setAttribute("invalid_signature", e.getMessage());
        } catch (Exception e) {
            logger.warn("Failed to process JWT token: " + e.getMessage());
        }

        chain.doFilter(request, response);
    }
}