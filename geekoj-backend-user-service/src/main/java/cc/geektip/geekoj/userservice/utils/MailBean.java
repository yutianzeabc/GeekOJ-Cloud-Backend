package cc.geektip.geekoj.userservice.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serial;
import java.io.Serializable;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MailBean implements Serializable {
    private String recipient;//邮件接收人
    private String subject; //邮件主题
    private String content; //邮件内容
    @Serial
    private static final long serialVersionUID = 1L;
}
