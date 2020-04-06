package cn.edu.swpu.wlzx.consumer.service;

import cn.edu.swpu.wlzx.consumer.model.SysRecord;
import org.springframework.data.domain.Page;

/**
 * @author gongsir
 * @date 2020/4/5 10:38
 * 编码不要畏惧变化，要拥抱变化
 */
public interface IRecordService {
    /**
     * 增加计算记录
     * @param record 计算信息
     */
    void saveRecord(SysRecord record);

    /**
     * 条件查询
     * @param page 当前页
     * @param size 每页数量
     * @param username 用户名
     * @return Page
     */
    Page<SysRecord> findAllRecord(int page,
                                  int size,
                                  String username);
}
