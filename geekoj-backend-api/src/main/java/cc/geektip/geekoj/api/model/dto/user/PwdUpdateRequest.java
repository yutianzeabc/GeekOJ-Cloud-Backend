package cc.geektip.geekoj.api.model.dto.user;

import lombok.Data;

@Data
public class PwdUpdateRequest {
    private String originalPwd;
    private String newPwd;
}
