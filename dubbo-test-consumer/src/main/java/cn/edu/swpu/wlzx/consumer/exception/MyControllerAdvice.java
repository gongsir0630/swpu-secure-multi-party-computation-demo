package cn.edu.swpu.wlzx.consumer.exception;

import cn.edu.swpu.wlzx.consumer.common.utils.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author gongsir
 * @date 2020/3/23 12:17
 * 编码不要畏惧变化，要拥抱变化
 */
@ControllerAdvice
@ResponseBody
public class MyControllerAdvice {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 自定义异常处理
     * @param ex exception
     * @return result
     */
    @ExceptionHandler(value = MyException.class)
    public ResponseEntity<Result> myErrorHandler(MyException ex) {
        logger.info("=====>> 异常处理：code:{}, msg:{}",ex.getErr_msg(),ex.getErr_msg());
        return ResponseEntity.ok(
                new Result(ex.getErr_code(),ex.getErr_msg())
        );
    }
}
