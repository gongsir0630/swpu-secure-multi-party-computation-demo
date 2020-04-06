package cn.edu.swpu.wlzx.consumer.repository;

import cn.edu.swpu.wlzx.consumer.model.SysRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author gongsir
 * @date 2020/4/5 10:35
 * 编码不要畏惧变化，要拥抱变化
 */
public interface RecordRepository extends JpaRepository<SysRecord,Integer>,
        JpaSpecificationExecutor<SysRecord> {
}
