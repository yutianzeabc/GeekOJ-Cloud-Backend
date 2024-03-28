package cc.geektip.geekoj.api.model.vo.user;

import lombok.Data;

@Data
public class UserTagVo {
    private Long id;
    private Long parentId;
    private String name;
    private String color;
}
