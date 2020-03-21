package cn.edu.swpu.wlzx.consumer.common.utils;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import javax.xml.bind.DatatypeConverter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author gongsir
 * @date 2020-3-21 20:32
 * 编码不要畏惧变化，要拥抱变化
 */
public class MiscUtil {
    /**
     * 解析错误字段
     * @param bindingResult 错误封装
     * @return 错误信息
     */
    public static Result getValidateError(BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            return null;
        }
        Map<String,String> fieldErrors = new HashMap<>();
        for (FieldError error:bindingResult.getFieldErrors()) {
            fieldErrors.put(error.getField(),error.getDefaultMessage());
        }
        Result res = new Result(422,"输入有误");
        res.putData("fieldErrors",fieldErrors);

        return res;
    }

    public static String toHexString(byte[] array) {
        return DatatypeConverter.printHexBinary(array);
    }

    public static byte[] toByteArray(String s) {
        return DatatypeConverter.parseHexBinary(s);
    }
}

