package cc.geektip.geekoj.userservice.controller;

import cc.geektip.geekoj.api.model.dto.user.UserTagAddRequest;
import cc.geektip.geekoj.api.model.vo.user.UserTagVo;
import cc.geektip.geekoj.api.model.vo.user.UserTagCategoryVo;
import cc.geektip.geekoj.api.service.user.UserTagService;
import cc.geektip.geekoj.common.common.R;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tags")
public class UserTagController {
    @Resource
    private UserTagService userTagService;

    /**
     * 获取所有标签
     * @return
     */
    @GetMapping
    public R<List<UserTagCategoryVo>> getAllTags(){
        List<UserTagCategoryVo> allTags = userTagService.getAllTags();
        return R.ok(allTags);
    }

    /**
     * 添加一个标签
     * @param userTagAddRequest
     * @return
     */
    @PutMapping
    public R<UserTagVo> addATag(@RequestBody UserTagAddRequest userTagAddRequest){
        UserTagVo userTagVo = userTagService.addATag(userTagAddRequest);
        return R.ok(userTagVo);
    }
}
