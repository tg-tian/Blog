package com.tg.blog.backend.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tg.blog.backend.common.ResponseEntity;
import com.tg.blog.backend.common.constants.RedisKeys;
import com.tg.blog.backend.constants.RoleConstants;
import com.tg.blog.backend.service.cache.RedisService;
import com.tg.blog.backend.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 管理接口鉴权拦截器
 * 使用Spring MVC Interceptor对文章、分类、标签、项目等管理接口进行鉴权
 */
@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private RedisService redisService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String method = request.getMethod();
        String uri = request.getRequestURI();

        // 放行文件相关接口
        if (pathMatcher.match("/file/**", uri)) {
            return true;
        }

        // 放行只读与预检请求
        if ("GET".equalsIgnoreCase(method) || "HEAD".equalsIgnoreCase(method) || "OPTIONS".equalsIgnoreCase(method)) {
            return true;
        }

        // 放行文章公开写操作
        if ("POST".equalsIgnoreCase(method) && (
                pathMatcher.match("/article/*/like", uri) ||
                pathMatcher.match("/article/*/comment", uri) ||
                pathMatcher.match("/article/*/view", uri)
        )) {
            return true;
        }

        // 其余写操作：拦截器直接进行JWT认证与管理员鉴权
        String authHeader = request.getHeader("Authorization");
        if (!StringUtils.hasText(authHeader) || !authHeader.startsWith("Bearer ")) {
            writeJson(response, ResponseEntity.failure(401, "未登录或令牌缺失"));
            return false;
        }

        String token = authHeader.substring(7);
        if (!jwtUtil.validateToken(token)) {
            writeJson(response, ResponseEntity.failure(401, "令牌无效或已过期"));
            return false;
        }

        Claims claims;
        try {
            claims = jwtUtil.parseClaims(token);
        } catch (Exception e) {
            writeJson(response, ResponseEntity.failure(401, "令牌解析失败"));
            return false;
        }

        String username = claims.getSubject();
        String role = claims.get("role", String.class);
        // 与Redis中的当前有效token匹配，保证会话一致性
        String key = RedisKeys.AUTH_USER_TOKEN_PREFIX + username;
        String cachedToken = redisService.get(key);
        if (cachedToken == null || "0".equals(cachedToken) || !token.equals(cachedToken)) {
            writeJson(response, ResponseEntity.failure(401, "登录状态已失效，请重新登录"));
            return false;
        }

        boolean isAdmin = RoleConstants.ADMIN.equals(role);
        if (!isAdmin) {
            writeJson(response, ResponseEntity.failure(403, "无权限访问该管理接口"));
            return false;
        }

        return true;
    }

    private void writeJson(HttpServletResponse response, ResponseEntity<?> body) throws IOException {
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType("application/json;charset=UTF-8");
        // 同步HTTP状态码与响应体code
        response.setStatus(body.getCode() != null ? body.getCode() : 500);
        response.getWriter().write(objectMapper.writeValueAsString(body));
        response.getWriter().flush();
    }
}