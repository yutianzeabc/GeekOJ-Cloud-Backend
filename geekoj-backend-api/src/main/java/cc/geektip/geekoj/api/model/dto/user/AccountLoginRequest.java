package cc.geektip.geekoj.api.model.dto.user;

import lombok.Data;

@Data
public class AccountLoginRequest {
    private String account;
    private String password;
}