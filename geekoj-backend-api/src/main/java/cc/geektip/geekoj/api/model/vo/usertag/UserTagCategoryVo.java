package cc.geektip.geekoj.api.model.vo.usertag;

import com.antares.member.model.entity.UserTag;
import lombok.Data;

import java.util.List;

@Data
public class UserTagCategoryVo {
    private Long id;

    private String name;

    private List<UserTag> tags;
}
