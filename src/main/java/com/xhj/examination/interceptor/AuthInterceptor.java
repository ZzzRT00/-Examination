package com.xhj.examination.interceptor;

import com.xhj.examination.entity.User;
import com.xhj.examination.service.UserService;
import com.xhj.examination.utils.JwtUtil;
import com.xhj.examination.utils.UserHolder;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        if(uri.equals("/user/login")){
            return true;
        }

        String token = request.getHeader("Authorization");
        if(token == null || token.isEmpty()){
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write("{\"code\":401,\"msg\":\"请登录\"}");
            return false;
        }
        Claims claims = JwtUtil.parse(token);
        if(claims == null){
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write("{\"code\":401,\"msg\":\"token失效，请重新登录\"}");
            return false;
        }
        Long userId = Long.valueOf(claims.getSubject());
        User loginUser = userService.getById(userId);
        if(loginUser == null){
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write("{\"code\":401,\"msg\":\"用户不存在\"}");
            return false;
        }

        UserHolder.set(loginUser);

        Object roleObj = claims.get("role");
        String role = roleObj == null ? "" : roleObj.toString();
        // 兼容数据库中存的中文身份：统一映射为英文角色再校验
        if ("教师".equals(role)) {
            role = "teacher";
        } else if ("学生".equals(role)) {
            role = "student";
        }

        if(uri.contains("/teacher/") && !"teacher".equals(role)){
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write("{\"code\":403,\"msg\":\"无教师权限\"}");
            return false;
        }

        if(uri.contains("/student/") && !"student".equals(role)){
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().write("{\"code\":403,\"msg\":\"无学生权限\"}");
            return false;
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        UserHolder.remove();
    }
}
