package cn.edu.swpu.wlzx.api.compute;

import cn.edu.swpu.wlzx.domain.Algorithm;

import java.util.List;

/**
 * 浪潮模型管理接口
 * @author gongsir
 */
public interface LcService {

    /**
     * 测试方法
     * @param name name
     * @return String.class
     */
    String sayHello(String name);

    /**
     * 添加算法模型
     * @param algorithm 模型信息
     * @return 模型al
     */
    Algorithm insertAl(Algorithm algorithm);

    /**
     * 根据id删除模型
     * @param id 模型id
     */
    void deleteAlById(Integer id);

    /**
     * 根据id获取模型信息
     * @param id 模型id
     * @return Algorithm
     */
    Algorithm findAlById(Integer id);

    /**
     * 通过链接匹配
     * @param url 链接，唯一
     * @return algorithm
     */
    Algorithm findByUrl(String url);

    /**
     * 条件查询
     * @param name 模型名称
     * @param status 发布状态
     * @return List<Algorithm>
     */
    List<Algorithm> findAll(String name,
                            String status);

    /**
     * 更新al模型信息
     * @param algorithm 模型信息（需要修改的字段字段封装）
     * @return 修改后的Algorithm
     */
    Algorithm updateAl(Algorithm algorithm);
}