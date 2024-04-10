package cc.geektip.geekoj.userservice.controller;

import cc.geektip.geekoj.api.model.vo.user.SocialUser;
import cc.geektip.geekoj.api.service.user.UserService;
import cc.geektip.geekoj.common.utils.HttpUtils;
import cc.geektip.geekoj.userservice.config.GiteeOAuthProperties;
import com.alibaba.fastjson2.JSON;
import jakarta.annotation.Resource;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/oauth2.0")
public class OAuth2Controller {

    @Resource
    private GiteeOAuthProperties giteeOAuthProperties;

    @Resource
    private UserService userService;

    /**
     * 第三方登录成功后的回调
     * @param code
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/gitee/success")
    public String gitee(@RequestParam("code") String code) throws Exception {
        Map<String, String> map = new HashMap<>();

        map.put("client_id", giteeOAuthProperties.getClientId());
        map.put("client_secret", giteeOAuthProperties.getClientSecret());
        map.put("grant_type", giteeOAuthProperties.getGrantType());
        map.put("redirect_uri", giteeOAuthProperties.getRedirectUri());
        map.put("code", code);

        // 1、根据用户授权返回的code换取access_token
        HttpResponse res = HttpUtils.doPost("https://gitee.com", "/oauth/token", "post", new HashMap<>(), map, new HashMap<>());
        // 2、OAuthLogin
        if (res.getStatusLine().getStatusCode() == 200) {
            // 获取到了access_token,转为社交登录对象
            String json = EntityUtils.toString(res.getEntity());
            SocialUser socialUser = JSON.parseObject(json, SocialUser.class);
            // 第一次使用社交帐号登录自动注册
            userService.oauthLogin(socialUser);
            return "redirect:https://oj.geektip.cc";
        }
        return "redirect:https://oj.geektip.cc/user/register";
    }
}
