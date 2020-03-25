package cn.edu.swpu.wlzx.consumer.mail.impl;

import cn.edu.swpu.wlzx.consumer.mail.MailService;
import cn.edu.swpu.wlzx.consumer.common.utils.AuthCodeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.concurrent.TimeUnit;

/**
 * @author gongsir
 * @date 2020/3/25 14:32
 * 编码不要畏惧变化，要拥抱变化
 */
@Service
public class MailServiceImpl implements MailService {
    @Resource
    private JavaMailSender mailSender;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Value("${spring.mail.username}")
    private String from;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 发送邮件验证码
     *
     * @param to 邮件接收人地址
     * @return 发送状态
     */
    @Override
    public Boolean sendAuthCode(String to) {
        if (to.isEmpty()){
            return Boolean.FALSE;
        }

        // 先从redis查询是否存在刚刚生成的验证码
        String code = redisTemplate.opsForValue().get(to);
        if (null == code) {
            // 重新生成 6 位数字验证码
            code = AuthCodeUtil.getCode(6);
            // 加入缓存，3 分钟有效
            redisTemplate.opsForValue().set(to,code,3, TimeUnit.MINUTES);
        }

        MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
        try {
            // 邮件发送方
            messageHelper.setFrom(from);
            // 邮件主题
            messageHelper.setSubject("网信中心-多方数据安全计算平台");
            // 邮件接收方
            messageHelper.setTo(to);
            //邮件内容
            messageHelper.setText("尊敬的用户您好, 您的验证码："+code+", 验证码3分钟内有效, 过期作废。如非本人操作, 请忽略本邮件。");
            // 发送
            this.mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            logger.error("send mail to {} failure,for {}",to,e);
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
}
