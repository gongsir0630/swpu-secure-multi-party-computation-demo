package cn.edu.swpu.wlzx.consumer.common.utils;

import java.util.Random;

/**
 * @author gongsir
 * @date 2020/3/25 14:51
 * 编码不要畏惧变化，要拥抱变化
 */
public class AuthCodeUtil {
    /**
     * 生成n位数字验证码
     * @param n 验证码位数
     * @return String code
     */
    public static String getCode(int n) {
        StringBuilder code = new StringBuilder();
        Random ran = new Random();
        for (int i=0; i<n; i++){
            code.append(Integer.valueOf(ran.nextInt(10)).toString());
        }
        return code.toString();
    }
}
