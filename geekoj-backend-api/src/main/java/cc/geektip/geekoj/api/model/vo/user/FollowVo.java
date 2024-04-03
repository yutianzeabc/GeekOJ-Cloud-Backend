package cc.geektip.geekoj.api.model.vo.user;

import lombok.Data;

import java.io.Serializable;

@Data
public class FollowVo implements Serializable {
    private Long uid;
    private String username;
    private String avatar;
    private Integer unread;
}
