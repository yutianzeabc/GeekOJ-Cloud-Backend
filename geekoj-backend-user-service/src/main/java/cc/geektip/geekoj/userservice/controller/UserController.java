package cc.geektip.geekoj.userservice.controller;

import cc.geektip.geekoj.api.model.dto.user.PwdUpdateRequest;
import cc.geektip.geekoj.api.model.dto.user.UserUpdateRequest;
import cc.geektip.geekoj.api.model.vo.user.UserInfoVo;
import cc.geektip.geekoj.api.service.user.UserService;
import cc.geektip.geekoj.common.common.R;
import cc.geektip.geekoj.userservice.utils.SessionUtils;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class UserController {
    @Resource
    private UserService userService;

    @Resource
    private SessionUtils sessionUtils;

    /**
     * 获取当前登录用户的信息
     * @return
     */
    @GetMapping("/info")
    public R<UserInfoVo> getCurrentUser(){
        UserInfoVo currentUser = sessionUtils.getCurrentUserPermitNull();
        return R.ok(currentUser);
    }

    /**
     * 更新当前用户的基本信息
     * @param updateVo
     * @return
     */
    @PostMapping("/update")
    public R update(@RequestBody UserUpdateRequest updateVo){
        userService.updateCurrentUser(updateVo);
        return R.ok();
    }

    /**
     * 更新用户的密码
     * @param pwdUpdateRequest
     * @return
     */
    @PutMapping("/pwd")
    public R updatePwd(@RequestBody PwdUpdateRequest pwdUpdateRequest){
        userService.updatePwd(pwdUpdateRequest);
        return R.ok();
    }

    /**
     * 更新用户绑定的手机
     * @param phone
     * @param captcha
     * @return
     */
    @PutMapping("/phone")
    public R bindPhone(@Pattern(regexp = "^1([3-9])[0-9]{9}$", message = "手机号格式不正确") @RequestParam String phone,
                       @Length(min = 6, max = 6, message = "验证码格式不正确") @RequestParam String captcha){
        userService.bindPhone(phone, captcha);
        return R.ok();
    }

    /**
     * 更新用户绑定的邮箱
     * @param email
     * @param captcha
     * @return
     */
    @PutMapping("/email")
    public R updateMail(@Email @RequestParam String email,
                       @Length(min = 6, max = 6, message = "验证码格式不正确") @RequestParam String captcha){
        userService.updateMail(email, captcha);
        return R.ok();
    }

    /**
     * 根据id获取用户信息，包含当前用户是否关注了该用户的信息
     * @param uid
     * @return
     */
    @GetMapping("/info/{uid}")
    public R<UserInfoVo> info(@PathVariable("uid") Long uid){
        UserInfoVo userByUid = userService.getUserByUid(uid);
        return R.ok(userByUid);
    }
}
