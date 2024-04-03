package cc.geektip.geekoj.api.model.dto.user;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

import java.io.Serializable;

@Data
public class UserRegisterRequest implements Serializable {
    @Length(min = 6,max = 18,message = "密码必须是6—18位字符")
    private String password;

    @Email
    private String email;

    @NotEmpty(message = "验证码不能为空")
    private String captcha;
}