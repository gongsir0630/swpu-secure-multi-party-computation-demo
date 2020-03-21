package cn.edu.swpu.wlzx.consumer.exception;

/**
 * @author gongsir
 * @date 2020/3/21 18:17
 * 编码不要畏惧变化，要拥抱变化
 */
public class MyException extends RuntimeException {
    private int err_code;
    private String err_msg;

    public MyException(int err_code,String err_msg) {
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
}
