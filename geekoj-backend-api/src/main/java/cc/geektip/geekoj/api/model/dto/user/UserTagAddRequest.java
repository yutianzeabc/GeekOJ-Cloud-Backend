package cc.geektip.geekoj.api.model.dto.user;

import lombok.Data;

@Data
public class UserTagAddRequest {
    private Long parentId;
    private String name;
}
