package cc.geektip.geekoj.userservice.utils;

import cc.geektip.geekoj.common.common.AppHttpCodeEnum;
import cc.geektip.geekoj.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;

@Slf4j
@Component
public class MailUtil {
    @Value("${spring.mail.username}")
    private String MAIL_SENDER; //邮件发送者
    @Resource
    private JavaMailSender javaMailSender;

    public void sendMail(String mail, String code){
        MailBean mailBean = new MailBean();
        mailBean.setRecipient(mail);//接收者
        mailBean.setSubject("GeekOJ 验证码");//标题
        //内容主体
        mailBean.setContent("您的验证码为：" + code + "，有效期10分钟。");
        try {
            SimpleMailMessage mailMessage= new SimpleMailMessage();
            mailMessage.setFrom(MAIL_SENDER);//发送者
            mailMessage.setTo(mailBean.getRecipient());//接收者
            mailMessage.setSubject(mailBean.getSubject());//邮件标题
            mailMessage.setText(mailBean.getContent());//邮件内容
            javaMailSender.send(mailMessage);//发送邮箱
        } catch (Exception e) {
            log.error("邮件发送失败", e);
            throw new BusinessException(AppHttpCodeEnum.INTERNAL_SERVER_ERROR, "邮件发送失败");
        }
    }
}
