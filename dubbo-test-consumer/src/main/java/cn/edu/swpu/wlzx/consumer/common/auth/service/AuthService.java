package cn.edu.swpu.wlzx.consumer.common.auth.service;

import cn.edu.swpu.wlzx.domain.User;

/**
 * @author gongsir
 * @date 2020/3/21 20:36
 * 编码不要畏惧变化，要拥抱变化
 */
public interface AuthService {
    /**
     * 用户注册
     * @param user user
     * @return id
     */
    User register(User user);

    /**
     * 登录验证
     * @param username 用户账号
     * @param password 用户密码
     * @return 返回token
     */
    String login(String username,String password);
}
