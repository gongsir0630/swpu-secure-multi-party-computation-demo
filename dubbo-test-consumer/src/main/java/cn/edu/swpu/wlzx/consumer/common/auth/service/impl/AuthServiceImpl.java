package cn.edu.swpu.wlzx.consumer.common.auth.service.impl;

import cn.edu.swpu.wlzx.api.user.UserService;
import cn.edu.swpu.wlzx.consumer.common.auth.AuthUser;
import cn.edu.swpu.wlzx.consumer.common.auth.service.AuthService;
import cn.edu.swpu.wlzx.consumer.common.utils.JwtUtil;
import cn.edu.swpu.wlzx.domain.User;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author gongsir
 * @date 2020/3/21 20:37
 * 编码不要畏惧变化，要拥抱变化
 */
@Service
public class AuthServiceImpl implements AuthService {

    private final Logger logger =
            LoggerFactory.getLogger(getClass());

    @Reference(version = "1.0.0")
    private UserService userService;

    @Resource
    private PasswordEncoder passwordEncoder;

    private AuthenticationManager authenticationManager;
    private UserDetailsService userDetailsService;
    private JwtUtil jwtUtil;

    public AuthenticationManager getAuthenticationManager() {
        return authenticationManager;
    }

    public UserDetailsService getUserDetailsService() {
        return userDetailsService;
    }

    public JwtUtil getJwtUtil() {
        return jwtUtil;
    }

    public AuthServiceImpl(AuthenticationManager authenticationManager, UserDetailsService userDetailsService, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * 用户注册
     *
     * @param user user
     * @return id
     */
    @Override
    public User register(User user) {
        // 密码处理
        user.setPassword(passwordEncoder.encode(
                user.getPassword()
        ));
        return userService.saveUser(user);
    }

    /**
     * 登录验证
     *
     * @param username 用户账号
     * @param password 用户密码
     * @return 返回token
     */
    @Override
    public String login(String username, String password) {
        // 认证用户，认证失败抛出异常，由JwtAuthError的commence类返回401
        UsernamePasswordAuthenticationToken upToken =
                new UsernamePasswordAuthenticationToken(username,password);

        Authentication authentication = authenticationManager.authenticate(upToken);
        // 存储认证信息
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // generate token
        AuthUser authUser = (AuthUser) userDetailsService.loadUserByUsername(username);
        return jwtUtil.generateToken(authUser.getUser());
    }
}
