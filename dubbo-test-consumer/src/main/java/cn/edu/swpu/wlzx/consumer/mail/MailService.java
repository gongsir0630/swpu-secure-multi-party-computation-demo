package cn.edu.swpu.wlzx.consumer.mail;

/**
 * 描述：邮件服务
 * @author gongsir
 * @date 2020/3/25 14:30
 * 编码不要畏惧变化，要拥抱变化
 */
public interface MailService {
    /**
     * 发送邮件验证码
     * @param to 邮件接收人地址
     * @return 发送状态
     */
    Boolean sendAuthCode(String to);
}
