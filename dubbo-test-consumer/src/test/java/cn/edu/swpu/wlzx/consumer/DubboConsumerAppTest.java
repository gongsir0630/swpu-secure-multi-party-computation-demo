package cn.edu.swpu.wlzx.consumer;

import cn.edu.swpu.wlzx.api.compute.ComputeService;
import cn.edu.swpu.wxzx.keys.KeyService;
import cn.edu.swpu.wxzx.keys.UserService;
import org.apache.dubbo.config.annotation.Reference;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author gongsir
 * @date 2020/4/2 17:24
 * 编码不要畏惧变化，要拥抱变化
 */
@SpringBootTest
public class DubboConsumerAppTest {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Reference(version = "1.0.0")
    private KeyService keyService;
    @Reference(version = "1.0.0")
    private UserService userService;
    @Reference(version = "1.0.0",group = "test1")
    private ComputeService computeService;

    @Test
    public void testCompute() {
        logger.info("开始");
//        Map<String,String> params = new HashMap<>();
//        String username = "201731061426";
//        // 获取公钥key
//        String key = userService.insert(username);
//        // 直接生成 A
//        String A = keyService.encode(String.valueOf(10), key);
//        // 直接生成 B
//        String B = keyService.encode(String.valueOf(20), key);
//        // 封装参数
//        params.put("username",username);
//        params.put("publicKey",key);
//        params.put("A",A);
//        params.put("B",B);
//        logger.info("计算结果：{}",computeService.compute(params));
    }
}