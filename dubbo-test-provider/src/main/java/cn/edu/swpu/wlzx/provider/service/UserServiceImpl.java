package cn.edu.swpu.wlzx.provider.service;

import cn.edu.swpu.wlzx.api.UserService;
import cn.edu.swpu.wlzx.domain.User;
import cn.edu.swpu.wlzx.provider.repository.UserRepository;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author gongsir
 * @date 2020/3/21 14:50
 * 编码不要畏惧变化，要拥抱变化
 */
@Service(version = "1.0.0")
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    /**
     * 注册User
     *
     * @param user user信息
     * @return user
     */
    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    /**
     * 查询user
     *
     * @param username 账号
     * @return user
     */
    @Override
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
