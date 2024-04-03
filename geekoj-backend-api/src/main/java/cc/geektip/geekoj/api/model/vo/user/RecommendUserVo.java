package cc.geektip.geekoj.api.model.vo.user;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class RecommendUserVo implements Serializable {
    private UserInfoVo userInfo;
    private Double score;
}
