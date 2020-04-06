package cn.edu.swpu.wlzx.consumer.config;

import cn.edu.swpu.wlzx.consumer.common.auth.JwtAuthError;
import cn.edu.swpu.wlzx.consumer.common.auth.JwtAuthFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsUtils;

/**
 * 描述：安全认证配置
 * @author gongsir
 * @date 2020/3/21 17:48
 * 编码不要畏惧变化，要拥抱变化
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 实现了DetailsService接口，用来做登陆验证
     */
    @Autowired
    private UserDetailsService CustomUserDetailsService;

    /**
     * 认证失败处理类、权限不足处理类，包含认证错误与鉴权错误处理
     */
    @Autowired
    private JwtAuthError authErrorHandler;

    /**
     * 密码明文加密方式配置
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * jwt校验过滤器，从http头部Authorization字段读取token并校验
     */
    @Bean
    public JwtAuthFilter authFilter()  {
        return new JwtAuthFilter();
    }

    /**
     * 获取AuthenticationManager（认证管理器），可以在其他地方使用
     * @return authenticationManagerBean
     * @throws Exception exception
     */
    @Bean(name = "authenticationManagerBean")
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 认证用户时用户信息加载配置，注入自定义 UserDetailsService
        auth
                // 设置UserDetailsService
                .userDetailsService(CustomUserDetailsService)
                // 使用BCrypt进行密码的hash
                .passwordEncoder(passwordEncoder());
    }

    /**
     * 配置http，包含权限配置
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                // 由于使用的是JWT，我们这里不需要csrf
                .csrf().disable()
                // 基于token，不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // 设置authErrorHandler处理认证失败、鉴权失败
                .exceptionHandling().authenticationEntryPoint(authErrorHandler).accessDeniedHandler(authErrorHandler)
                .and()
                // 开始设置权限
                .authorizeRequests()
                //处理跨域请求中的prefLight请求
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                // 平台需要登录
                .antMatchers("/user/**").authenticated()
                // 政府角色
                .antMatchers("/gov/**").hasRole("GOV")
                // 浪潮角色
                .antMatchers("/lc/**").hasRole("LANG")
                // 平台管理员
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                // 除上面外的所有请求全部放开
                .anyRequest().permitAll();
        // 添加JWT过滤器，JWT过滤器在用户名密码认证过滤器之前
        http.addFilterBefore(authFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    /**
     * 放行swagger等静态资源
     * @param web webApp
     */
    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/v2/api-docs",
                "/swagger-resources/configuration/ui",
                "/swagger-resources",
                "/swagger-resources/configuration/security",
                "/swagger-ui.html",
                "/uploadImg/**",
                "/static/**"
        );
    }
}
