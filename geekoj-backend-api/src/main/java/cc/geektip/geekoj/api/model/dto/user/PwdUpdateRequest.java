package cc.geektip.geekoj.api.model.dto.user;

import lombok.Data;

import java.io.Serializable;

@Data
public class PwdUpdateRequest implements Serializable {
    private String originalPwd;
    private String newPwd;
}
