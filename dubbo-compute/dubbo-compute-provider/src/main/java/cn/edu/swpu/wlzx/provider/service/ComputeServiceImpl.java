package cn.edu.swpu.wlzx.provider.service;

import cn.edu.swpu.wlzx.api.compute.ComputeService;
import cn.edu.swpu.wxzx.keys.KeyService;
import cn.edu.swpu.wxzx.keys.UserService;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.Map;

/**
 * @author gongsir
 * @date 2020/4/2 16:36
 * 编码不要畏惧变化，要拥抱变化
 */
@Service(version = "1.0.0",group = "test1")
public class ComputeServiceImpl implements ComputeService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 引入密钥服务
     */
    @Reference(version = "1.0.0")
    private KeyService keyService;
    @Reference(version = "1.0.0")
    private UserService userService;

    /**
     * 个人评估授额计算模型实现
     * 模型：Math.max(Math.min(Math.max(A * 0.8，B * 15) * 10，150000)，50000)
     * 1、计算 A*8 和 B*150 的结果并比较大小，求出最大值为max
     * 2、比较 max 和 150000，求出最小值 min
     * 3、比较 min 和 50000，求出最大值 code
     * 4、解密code
     * 5、返回明文result
     * @param params 模型计算需要的参数
     * @return 统一返回字符串
     */
    @Override
    public String compute(Map<String, String> params) {
        logger.info("计算参数和用户信息：{}",params);

        // 来自政府 A 的加密数据
        String a = params.get("A");
        // 来自政府 B 的加密数据
        String b = params.get("B");
        // 公钥
        String publicKey = params.get("publicKey");
        // 用户
        String username = params.get("username");
        // 计算 A 和 encode(8) 的乘积
        BigInteger one = new BigInteger(a).multiply(new BigInteger(keyService.encode(String.valueOf(8), publicKey)));
        logger.info("one:{}",one);
        // 计算 B 和 encode(150) 的乘积
        BigInteger two = new BigInteger(b).multiply(new BigInteger(keyService.encode(String.valueOf(150), publicKey)));
        logger.info("two:{}",two);
        // 比较大小
        String max = userService.getMax(username, one.toString(), two.toString(), publicKey);
        logger.info("max:{}",max);
        // 比较 max 和 150000，求出最小值 min
        String min = userService.getMin(username, max,
                keyService.encode(String.valueOf(150000), publicKey), publicKey);
        logger.info("min:{}",min);
        // 比较 min 和 50000，求出最大值 code
        String code = userService.getMax(username, min,
                keyService.encode(String.valueOf(50000), publicKey), publicKey);
        logger.info("code:{}",code);
        // 返回密文
        return code;
    }
}
