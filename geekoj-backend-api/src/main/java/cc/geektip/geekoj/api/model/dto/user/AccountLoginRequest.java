package cc.geektip.geekoj.api.model.dto.user;

import lombok.Data;

import java.io.Serializable;

@Data
public class AccountLoginRequest implements Serializable {
    private String account;
    private String password;
}