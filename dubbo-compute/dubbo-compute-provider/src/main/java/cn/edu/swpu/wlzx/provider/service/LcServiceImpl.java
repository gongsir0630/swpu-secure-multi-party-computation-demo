package cn.edu.swpu.wlzx.provider.service;

import cn.edu.swpu.wlzx.api.compute.LcService;
import cn.edu.swpu.wlzx.domain.Algorithm;
import cn.edu.swpu.wlzx.provider.repository.AlRepository;
import org.apache.dubbo.config.annotation.Service;
import org.apache.dubbo.rpc.RpcContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * 描述：浪潮模型管理服务接口
 * @author gongsir
 */
@Service(version = "1.0.0")
public class LcServiceImpl implements LcService {
    private static final Logger logger = LoggerFactory.getLogger(LcServiceImpl.class);

    @Autowired
    private AlRepository alRepository;

    @Override
    public String sayHello(String name) {
        logger.info("Hello " + name + ", request from consumer: " + RpcContext.getContext().getRemoteAddress());
        return "Hello " + name + ", response from provider: " + RpcContext.getContext().getLocalAddress();
    }

    /**
     * 添加算法模型
     *
     * @param algorithm 模型信息
     * @return 模型al
     */
    @Override
    public Algorithm insertAl(Algorithm algorithm) {
        return alRepository.save(algorithm);
    }

    /**
     * 根据id删除模型
     *
     * @param id 模型id
     */
    @Override
    public void deleteAlById(Integer id) {
        alRepository.deleteById(id);
    }

    /**
     * 根据id获取模型信息
     *
     * @param id 模型id
     * @return Algorithm
     */
    @Override
    public Algorithm findAlById(Integer id) {
        Optional<Algorithm> optional = alRepository.findById(id);
        return optional.orElse(null);
    }

    /**
     * 通过链接匹配
     *
     * @param url 链接，唯一
     * @return algorithm
     */
    @Override
    public Algorithm findByUrl(String url) {
        Optional<Algorithm> optional = alRepository.findAlgorithmByUrlEndingWith(url);
        return optional.orElse(null);
    }

    /**
     * 条件查询
     * @param name 模型名称
     * @param status 发布状态
     * @return List<Algorithm>
     */
    @Override
    public List<Algorithm> findAll(String name,
                                   String status) {
        // 将匹配对象封装成Example对象
        Algorithm algorithm = new Algorithm();
        if (null != name && !"".equalsIgnoreCase(name.trim())) {
            algorithm.setName(name);
        }
        if (null != status && !"".equalsIgnoreCase(status.trim())) {
            algorithm.setStatus(status);
        }
        // 创建匹配器
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                // name模糊查询
                .withMatcher("name", ExampleMatcher.GenericPropertyMatcher::contains)
                // status精确匹配
                .withMatcher("status", ExampleMatcher.GenericPropertyMatcher::exact);
        // 创建实例
        Example<Algorithm> example = Example.of(algorithm,exampleMatcher);
        // 查询返回
        return alRepository.findAll(example);
    }

    /**
     * 更新al模型信息
     *
     * @param algorithm 模型信息（需要修改的字段字段封装）
     * @return 修改后的Algorithm
     */
    @Override
    public Algorithm updateAl(Algorithm algorithm) {
        // 获取数据库实体
        Optional<Algorithm> optional = alRepository.findById(algorithm.getId());
        if (optional.isPresent()) {
            Algorithm algorithm1 = optional.get();
            // 复制不为null的字段
            BeanUtils.copyProperties(algorithm,algorithm1,getNullPropertyNames(algorithm));
            // 保存更新
            alRepository.save(algorithm1);
            optional = alRepository.findById(algorithm.getId());
        }
        return optional.orElse(null);
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
