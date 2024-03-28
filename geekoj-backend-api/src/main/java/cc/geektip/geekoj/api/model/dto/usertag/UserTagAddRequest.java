package cc.geektip.geekoj.api.model.dto.usertag;

import lombok.Data;

@Data
public class UserTagAddRequest {
    private Long parentId;
    private String name;
}
