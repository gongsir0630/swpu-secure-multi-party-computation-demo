package cn.edu.swpu.wlzx.consumer.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 接口返回封装
 * @author gongsir
 * @date 2020-3-21 16:31
 * 编码不要畏惧变化，要拥抱变化
 */
public class Result implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(Result.class);
    private static final long serialVersionUID = 1L;

    private int err_code = -1;
    private String err_msg = "待处理";
    private Map<String, Object> data = new HashMap<String, Object>();

    public Result() {
    }

    public Result(int err_code, String err_msg) {
        this.err_code = err_code;
        this.err_msg = err_msg;
    }

    public int getErr_code() {
        return err_code;
    }

    public void setErr_code(int err_code) {
        this.err_code = err_code;
    }

    public String getErr_msg() {
        return err_msg;
    }

    public void setErr_msg(String err_msg) {
        this.err_msg = err_msg;
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public void putData(String key, Object value) {
        data.put(key,value);
    }

    public void removeData(String key) {
        data.remove(key);
    }

    @Override
    public String toString() {
        return "Result{" +
                "err_code=" + err_code +
                ", err_msg='" + err_msg + '\'' +
                ", data=" + data +
                '}';
    }
}
