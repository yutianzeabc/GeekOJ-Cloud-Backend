package cc.geektip.geekoj.api.model.dto.user;

import lombok.Data;

import java.io.Serializable;

@Data
public class UsernameAndAvatarDto implements Serializable {
    private Long uid;
    private String username;
    private String avatar;
}
