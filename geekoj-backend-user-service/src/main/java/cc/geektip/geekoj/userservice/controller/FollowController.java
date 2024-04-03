package cc.geektip.geekoj.userservice.controller;

import cc.geektip.geekoj.api.model.vo.user.FollowVo;
import cc.geektip.geekoj.api.model.vo.user.UserInfoVo;
import cc.geektip.geekoj.api.service.user.FollowService;
import cc.geektip.geekoj.common.common.R;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
public class FollowController {
    @Resource
    private FollowService followService;

    /**
     * 关注或取消关注
     * @param uid
     * @return
     */
    @PostMapping("/follow/{uid}")
    public R follow(@PathVariable("uid") Long uid){
        followService.follow(uid);
        return R.ok();
    }

    @GetMapping("/follows/of/current")
    public R<List<FollowVo>> getFollowsOfCurrent(){
        List<FollowVo> follows = followService.getFollowsOfCurrent();
        return R.ok(follows);
    }

    @GetMapping("/follows/of/{uid}")
    public R<List<UserInfoVo>> getFollowsByUid(@PathVariable("uid") Long uid){
        List<UserInfoVo> follows = followService.getFollowsByUid(uid);
        return R.ok(follows);
    }

    @GetMapping("/fans/of/{uid}")
    public R<List<UserInfoVo>> getFansByUid(@PathVariable("uid") Long uid){
        List<UserInfoVo> fans = followService.getFansByUid(uid);
        return R.ok(fans);
    }
}
