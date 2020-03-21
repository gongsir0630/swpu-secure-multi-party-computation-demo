package cn.edu.swpu.wlzx.consumer.common.auth;

import cn.edu.swpu.wlzx.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

/**
 * 描述：用户权限认证
 * @author gongsir
 * @date 2020/3/21 17:52
 * 编码不要畏惧变化，要拥抱变化
 */
public class AuthUser implements UserDetails {
    private static final long serialVersionUID = 1L;

    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public AuthUser(User user){
        this.setUser(user);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getAuthoritiesByRole(getUser().getRole());
    }

    public static Collection<? extends GrantedAuthority> getAuthoritiesByRole(String role) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        List<String> roles = Arrays.asList(role.split(","));
        if (roles.contains("user")) {
            // 普通用户角色
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }
        if (roles.contains("gov")) {
            // 政府管理员角色
            authorities.add(new SimpleGrantedAuthority("ROLE_GOV"));
        }
        if (roles.contains("langChao")) {
            // 浪潮管理员
            authorities.add(new SimpleGrantedAuthority("ROLE_LANG"));
        }
        if (roles.contains("admin")) {
            // 平台管理员
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        return getUser().getPassword();
    }

    @Override
    public String getUsername() {
        return getUser().getUsername();
    }

    /**
     * 账号是否没过期，过期的用户无法认证
     */
    @Override
    public boolean isAccountNonExpired() {
        return Boolean.TRUE;
    }

    /**
     * 账号是否没锁住，锁住的用户无法认证
     */
    @Override
    public boolean isAccountNonLocked() {
        return Boolean.TRUE;
    }

    /**
     * 密码是否没过期，密码过期的用户无法认证
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return Boolean.TRUE;
    }

    /**
     * 用户是否使能，未使能的用户无法认证
     */
    @Override
    public boolean isEnabled() {
        return Boolean.parseBoolean(getUser().getLoginStatus());
    }
}
