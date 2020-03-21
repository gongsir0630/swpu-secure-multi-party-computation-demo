package cn.edu.swpu.wlzx.consumer.common.auth;

import com.alibaba.fastjson.JSONObject;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author gongsir
 * @date 2020/2/27 15:04
 * 编码不要畏惧变化，要拥抱变化
 */
@Component
public class JwtAuthError implements AuthenticationEntryPoint, AccessDeniedHandler {
    /**
     * 认证失败处理，返回401
     * @param httpServletRequest 请求
     * @param httpServletResponse 回复
     * @param e exception
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        JSONObject rs = new JSONObject();
        rs.put("err_code",401);
        rs.put("err_msg","Unauthorized or invalid token");
        httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        httpServletResponse.getWriter().write(rs.toJSONString());
    }

    /**
     * 鉴权失败处理，返回403
     * @param httpServletRequest 请求
     * @param httpServletResponse 回复
     * @param e exception
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        JSONObject rs = new JSONObject();
        rs.put("err_code",403);
        rs.put("err_msg","Forbidden");
        httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        httpServletResponse.getWriter().write(rs.toJSONString());
    }
}
