package cc.geektip.geekoj.userservice.utils;

import cc.geektip.geekoj.common.common.AppHttpCodeEnum;
import cc.geektip.geekoj.common.exception.BusinessException;
import com.alibaba.fastjson.JSON;
import com.zhenzi.sms.ZhenziSmsClient;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Data
@Slf4j
@Component
@ConfigurationProperties(prefix = "geekoj.third-party.sms")
public class SmsUtils {
    private String apiUrl;
    private String appId;
    private String appSecret;
    private String templateId;

    public void sendCode(String phone, String code) {
        // 目前禁用短信发送
        throw new BusinessException(AppHttpCodeEnum.INTERNAL_SERVER_ERROR, "短信发送当前不可用");
    }

    public void sendCodeInner(String phone, String code) {

        try {
            // 认证榛子云接口
            ZhenziSmsClient client = new ZhenziSmsClient(apiUrl, appId, appSecret);

            // 发送短信
            Map<String, Object> params = new HashMap<>();
            params.put("number", phone);
            params.put("templateId", templateId);

            String[] templateParams = new String[2];
            templateParams[0] = code;
            templateParams[1] = "10";
            params.put("templateParams", templateParams);

            String result = client.send(params);
            Map map = JSON.parseObject(result, Map.class);
            int status = (int) map.get("code");
            if (status == 0) {
                log.info("短信发送成功！手机号：{}，验证码：{}", phone, code);
            }
        } catch (Exception e) {
            throw new BusinessException(AppHttpCodeEnum.INTERNAL_SERVER_ERROR, "短信发送失败");
        }
    }
}
