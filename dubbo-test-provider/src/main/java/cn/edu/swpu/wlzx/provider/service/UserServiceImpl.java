package cn.edu.swpu.wlzx.provider.service;

import cn.edu.swpu.wlzx.api.user.UserService;
import cn.edu.swpu.wlzx.domain.User;
import cn.edu.swpu.wlzx.provider.repository.UserRepository;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.Set;

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
     * 根据主键删除用户
     *
     * @param id id主键
     */
    @Override
    public void deleteUserById(Integer id) {
        userRepository.deleteById(id);
    }

    /**
     * 更新用户信息
     *
     * @param user 用户信息
     * @return 更新后的用户信息
     */
    @Override
    public User updateUser(User user) {
        User user1 = userRepository.findByUsername(user.getUsername());
        BeanUtils.copyProperties(user,user1,getNullPropertyNames(user));
        userRepository.save(user1);
        return userRepository.findByUsername(user1.getUsername());
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

    /**
     * 获取用户列表
     *
     * @param page     页码
     * @param size     数量
     * @param username 用户名
     * @return list
     */
    @Override
    public Page<User> findAllUser(int page, int size, String username) {
        User user = new User();
        if (null != username && !"".equals(username.trim())) {
            user.setUsername(username);
        }
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("username",ExampleMatcher.GenericPropertyMatcher::contains);
        Example<User> example = Example.of(user, matcher);
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "id");
        return userRepository.findAll(example,pageable);
    }

    /**
     * 筛选字段
     * @param source 源对象
     * @return 空字段
     */
    public static String[] getNullPropertyNames (Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for(PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }
}
