package cn.edu.swpu.wlzx.api.user;

import cn.edu.swpu.wlzx.domain.User;

/**
 * @author gongsir
 * @date 2020/3/21 14:44
 * 编码不要畏惧变化，要拥抱变化
 */
public interface UserService {
    /**
     * 注册User
     * @param user user信息
     * @return user
     */
    public User saveUser(User user);

    /**
     * 查询user
     * @param username 账号
     * @return user
     */
    public User findUserByUsername(String username);
}
