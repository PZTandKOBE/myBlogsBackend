package org.pztsblog.blogsend.interceptor;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.pztsblog.blogsend.utils.JwtUtils;
import org.pztsblog.blogsend.utils.UserContext;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 从请求头 (Header) 中获取 Token，前端一般把键命名为 Authorization
        String token = request.getHeader("Authorization");

        // 2. 如果前端按照规范加了 "Bearer " 前缀，我们把它截取掉
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        try {
            // 3. 校验并解析 Token
            if (token != null && !token.isEmpty()) {
                Claims claims = JwtUtils.parseToken(token);

                // 4. 从 Token 中拿出我们在 AuthController 登录时塞进去的 userId
                // 注意：由于 Token 里的数字可能被解析成 Integer，所以我们要稳妥地转换成 Long
                Long userId = claims.get("userId", Number.class).longValue();

                // 5. 把 userId 存进刚才写的“专属储物柜”
                UserContext.setUserId(userId);

                return true; // 校验通过，放行！
            }
        } catch (Exception e) {
            // 解析失败（Token 过期或者被篡改）会走到这里
            System.out.println("Token 解析失败: " + e.getMessage());
        }

        // 6. 如果没 Token 或者解析失败，直接打回 401 未授权
        response.setStatus(401);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"code\":401, \"message\":\"未登录或Token已过期\"}");
        return false; // 拦截，不让进 Controller
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 请求结束后，不管成功还是报错，都必须清空储物柜
        UserContext.removeUserId();
    }
}