package cc.geektip.geekoj.api.model.dto.user;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class UserUpdateRequest implements Serializable {
    private Long uid;
    private String username;
    private String password;
    private List<Long> tags;
    private String signature;
    private String email;
    private String phone;
    private Integer sex;
    private String avatar;
}
