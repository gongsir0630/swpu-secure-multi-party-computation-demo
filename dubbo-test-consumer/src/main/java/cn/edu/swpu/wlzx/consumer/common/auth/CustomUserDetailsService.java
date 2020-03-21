package cn.edu.swpu.wlzx.consumer.common.auth;

import cn.edu.swpu.wlzx.api.UserService;
import cn.edu.swpu.wlzx.consumer.exception.MyException;
import cn.edu.swpu.wlzx.domain.User;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author gongsir
 * @date 2020/3/21 18:10
 * 编码不要畏惧变化，要拥抱变化
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Reference(version = "1.0.0")
    UserService userService;

    /**
     * 根据用户账号从数据库加载用户信息
     * @param username 用户账号
     * @return authUser信息
     * @throws UsernameNotFoundException 找不到用户
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findUserByUsername(username);
        if (null == user) {
            logger.info("No user found with username {}",username);
            throw new MyException(401,"user not found!");
        }
        logger.info("查询到{}的信息：{}",username,user);
        return new AuthUser(user);
    }
}
