package cc.geektip.geekoj.api.model.vo.user;

import cc.geektip.geekoj.api.model.entity.user.UserTag;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class UserTagCategoryVo implements Serializable {
    private Long id;

    private String name;

    private List<UserTag> tags;
}
