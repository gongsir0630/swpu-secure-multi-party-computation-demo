package cn.edu.swpu.wlzx.provider.repository;

import cn.edu.swpu.wlzx.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author gongsir
 * @date 2020/3/21 14:51
 * 编码不要畏惧变化，要拥抱变化
 */
public interface UserRepository extends JpaRepository<User,Integer> {
    /**
     * 通过username查找用户
     * @param username 账号-唯一
     * @return user
     */
    User findByUsername(String username);
}
