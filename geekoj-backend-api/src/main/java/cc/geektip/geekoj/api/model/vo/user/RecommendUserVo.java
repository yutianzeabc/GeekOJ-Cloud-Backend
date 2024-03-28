package cc.geektip.geekoj.api.model.vo.user;

import com.antares.common.model.vo.UserInfoVo;
import lombok.Data;

@Data
public class RecommendUserVo {
    private UserInfoVo userInfo;
    private Double score;
}
