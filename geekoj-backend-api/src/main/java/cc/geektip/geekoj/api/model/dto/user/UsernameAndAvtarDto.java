package cc.geektip.geekoj.api.model.dto.user;

import lombok.Data;

@Data
public class UsernameAndAvtarDto {
    private Long uid;
    private String username;
    private String avatar;
}
