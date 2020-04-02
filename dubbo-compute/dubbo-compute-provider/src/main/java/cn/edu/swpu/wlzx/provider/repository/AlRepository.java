package cn.edu.swpu.wlzx.provider.repository;

import cn.edu.swpu.wlzx.domain.Algorithm;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author gongsir
 * @date 2020/3/28 16:23
 * 编码不要畏惧变化，要拥抱变化
 */
public interface AlRepository extends JpaRepository<Algorithm,Integer> {
}
