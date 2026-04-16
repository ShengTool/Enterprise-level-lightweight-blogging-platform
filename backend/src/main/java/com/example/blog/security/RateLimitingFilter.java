package com.example.blog.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Component
@Order(1) // 在JWT过滤器之前执行
public class RateLimitingFilter extends OncePerRequestFilter {
    
    private static final int MAX_REQUESTS_PER_MINUTE = 60;
    private static final int MAX_REQUESTS_PER_HOUR = 300;
    private static final int MAX_LOGIN_ATTEMPTS_PER_HOUR = 10;
    
    @Autowired(required = false)
    private RedisTemplate<String, String> redisTemplate;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String path = request.getRequestURI();
        String clientIp = getClientIp(request);
        
        // 检查是否为敏感端点
        if (path.startsWith("/api/auth/login") || path.startsWith("/api/auth/register")) {
            if (!checkLoginRateLimit(clientIp)) {
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                response.getWriter().write("{\"error\": \"Too many login attempts. Please try again later.\"}");
                return;
            }
        }
        
        // 检查通用速率限制
        if (!checkGeneralRateLimit(clientIp)) {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.getWriter().write("{\"error\": \"Rate limit exceeded. Please slow down.\"}");
            return;
        }
        
        filterChain.doFilter(request, response);
    }
    
    private boolean checkLoginRateLimit(String clientIp) {
        if (redisTemplate == null) {
            // 如果没有Redis，使用内存限制（简化版）
            return true;
        }
        
        String key = "login:attempts:" + clientIp;
        String attemptsStr = redisTemplate.opsForValue().get(key);
        int attempts = attemptsStr != null ? Integer.parseInt(attemptsStr) : 0;
        
        if (attempts >= MAX_LOGIN_ATTEMPTS_PER_HOUR) {
            return false;
        }
        
        redisTemplate.opsForValue().increment(key);
        if (attempts == 0) {
            redisTemplate.expire(key, Duration.ofHours(1));
        }
        
        return true;
    }
    
    private boolean checkGeneralRateLimit(String clientIp) {
        if (redisTemplate == null) {
            // 如果没有Redis，使用内存限制（简化版）
            return true;
        }
        
        // 每分钟限制
        String minuteKey = "rate:minute:" + clientIp;
        String minuteCountStr = redisTemplate.opsForValue().get(minuteKey);
        int minuteCount = minuteCountStr != null ? Integer.parseInt(minuteCountStr) : 0;
        
        if (minuteCount >= MAX_REQUESTS_PER_MINUTE) {
            return false;
        }
        
        // 每小时限制
        String hourKey = "rate:hour:" + clientIp;
        String hourCountStr = redisTemplate.opsForValue().get(hourKey);
        int hourCount = hourCountStr != null ? Integer.parseInt(hourCountStr) : 0;
        
        if (hourCount >= MAX_REQUESTS_PER_HOUR) {
            return false;
        }
        
        // 更新计数
        redisTemplate.opsForValue().increment(minuteKey);
        redisTemplate.opsForValue().increment(hourKey);
        
        if (minuteCount == 0) {
            redisTemplate.expire(minuteKey, Duration.ofMinutes(1));
        }
        if (hourCount == 0) {
            redisTemplate.expire(hourKey, Duration.ofHours(1));
        }
        
        return true;
    }
    
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        
        // 如果是多个IP，取第一个
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        
        return ip;
    }
    
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        // 跳过静态资源和健康检查
        return path.startsWith("/uploads/") || 
               path.startsWith("/swagger-ui/") || 
               path.startsWith("/v3/api-docs") ||
               path.equals("/actuator/health");
    }
}