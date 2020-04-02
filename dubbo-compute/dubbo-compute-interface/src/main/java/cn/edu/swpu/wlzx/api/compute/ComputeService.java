package cn.edu.swpu.wlzx.api.compute;

import java.util.Map;

/**
 * 描述：统一算法入口
 * @author gongsir
 * @date 2020/4/2 16:31
 * 编码不要畏惧变化，要拥抱变化
 */
public interface ComputeService {
    /**
     * 统一算法接口
     * @param params 模型计算需要的参数
     * @return 统一返回明文字符串
     */
    String compute(Map<String,String> params);
}
