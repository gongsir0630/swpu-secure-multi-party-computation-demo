package cn.edu.swpu.wlzx.consumer.service.impl;

import cn.edu.swpu.wlzx.consumer.model.SysRecord;
import cn.edu.swpu.wlzx.consumer.repository.RecordRepository;
import cn.edu.swpu.wlzx.consumer.service.IRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

/**
 * @author gongsir
 * @date 2020/4/5 10:46
 * 编码不要畏惧变化，要拥抱变化
 */
@Service
public class RecordServiceImpl implements IRecordService {

    @Autowired
    private RecordRepository recordRepository;

    /**
     * 增加计算记录
     *
     * @param record 计算信息
     */
    @Override
    public void saveRecord(SysRecord record) {
        recordRepository.save(record);
    }

    /**
     * 条件查询
     *
     * @param page     当前页
     * @param size     每页数量
     * @param username 用户名
     * @return Page
     */
    @Override
    public Page<SysRecord> findAllRecord(int page, int size, String username) {
        // 封装条件
        SysRecord record = new SysRecord();
        if (null != username && !"".equals(username)) {
            record.setUsername(username);
        }
        // 创建匹配器
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("username",
                        ExampleMatcher.GenericPropertyMatcher::contains);
        Example<SysRecord> example = Example.of(record, matcher);

        // 分页
//        Pageable pageable = new PageRequest(page,size,Sort.Direction.DESC,"id");
        Pageable pageable = PageRequest.of(page,size,Sort.Direction.DESC,"id");
        return recordRepository.findAll(example,pageable);
    }
}
