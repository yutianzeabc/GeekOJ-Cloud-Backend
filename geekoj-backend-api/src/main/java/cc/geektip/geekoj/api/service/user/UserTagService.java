package cc.geektip.geekoj.api.service.user;

import cc.geektip.geekoj.api.model.dto.user.UserTagAddRequest;
import cc.geektip.geekoj.api.model.entity.user.UserTag;
import cc.geektip.geekoj.api.model.vo.user.UserTagVo;
import cc.geektip.geekoj.api.model.vo.user.UserTagCategoryVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author Bill Yu
* @description 针对表【user_tag】的数据库操作Service
*/
public interface UserTagService extends IService<UserTag> {

    List<UserTagCategoryVo> getAllTags();

    UserTagVo addATag(UserTagAddRequest userTagAddRequest);

    List<UserTagVo> idsToTags(String idsJSON);
    List<UserTagVo> idsToTags(List<Long> tagIds);
}
