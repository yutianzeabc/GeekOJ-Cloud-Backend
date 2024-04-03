package cc.geektip.geekoj.userservice.controller;

import cc.geektip.geekoj.api.model.dto.user.AccountLoginRequest;
import cc.geektip.geekoj.api.model.dto.user.PhoneLoginRequest;
import cc.geektip.geekoj.api.model.dto.user.UserRegisterRequest;
import cc.geektip.geekoj.api.service.user.UserService;
import cc.geektip.geekoj.common.common.R;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static cc.geektip.geekoj.common.constant.SystemConstant.MAIL_CODE;
import static cc.geektip.geekoj.common.constant.SystemConstant.PHONE_CODE;

@Slf4j
@RestController
@RequestMapping("/")
@Validated
public class LoginController {
    @Resource
    private UserService userService;

    /**
     * 发送手机验证码
     * @param phone 手机号
     * @return
     */
    @GetMapping(value = "/sms/sendCode")
    public R sendCode(@Pattern(regexp = "^[1]([3-9])[0-9]{9}$", message = "手机号格式不正确") @RequestParam("phone") String phone) {
        //如果有错误回到注册页面
        userService.sendCode(phone, PHONE_CODE);
        return R.ok();
    }

    /**
     * 发送邮箱验证码
     * @param email
     * @return
     */
    @GetMapping(value = "/email/sendCode")
    public R sendMail(@Email @RequestParam("email") String email) {
        userService.sendCode(email, MAIL_CODE);
        return R.ok();
    }

    /**
     * 用户注册
     * @param userRegisterRequest
     * @return
     */
    @PostMapping(value = "/register")
    public R register(@Valid @RequestBody UserRegisterRequest userRegisterRequest) {
        userService.register(userRegisterRequest);
        return R.ok();
    }

    /**
     * 用户登录
     * @param accountLoginRequest
     * @return
     */
    @PostMapping(value = "/login")
    public R login(@RequestBody AccountLoginRequest accountLoginRequest) {
        userService.login(accountLoginRequest);
        return R.ok();
    }

    /**
     * 短信登录
     * @param phoneLoginRequest
     * @return
     */
    @PostMapping(value = "/loginByPhone")
    public R loginByPhone(@Valid @RequestBody PhoneLoginRequest phoneLoginRequest) {
        userService.loginByPhone(phoneLoginRequest);
        return R.ok();
    }


    /**
     * 登出
     * @return
     */
    @PostMapping(value = "/logout")
    public R logout() {
        userService.logout();
        return R.ok();
    }
}
