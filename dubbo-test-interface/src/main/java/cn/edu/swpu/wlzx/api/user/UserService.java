package cn.edu.swpu.wlzx.api.user;

import cn.edu.swpu.wlzx.domain.User;
import org.springframework.data.domain.Page;

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
    User saveUser(User user);

    /**
     * 根据主键删除用户
     * @param id id主键
     */
    void deleteUserById(Integer id);

    /**
     * 更新用户信息
     * @param user 用户信息
     * @return 更新后的用户信息
     */
    User updateUser(User user);

    /**
     * 查询user
     * @param username 账号
     * @return user
     */
    User findUserByUsername(String username);

    /**
     * 获取用户列表
     * @param page 页码
     * @param size 数量
     * @param username 用户名
     * @return list
     */
    Page<User> findAllUser(int page,int size,String username);
}
