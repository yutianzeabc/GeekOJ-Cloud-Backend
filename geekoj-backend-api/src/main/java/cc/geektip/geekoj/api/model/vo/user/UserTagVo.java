package cc.geektip.geekoj.api.model.vo.user;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserTagVo implements Serializable {
    private Long id;
    private Long parentId;
    private String name;
    private String color;
}
