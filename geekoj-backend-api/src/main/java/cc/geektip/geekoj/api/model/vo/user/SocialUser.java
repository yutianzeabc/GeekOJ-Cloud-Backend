package cc.geektip.geekoj.api.model.vo.user;

import lombok.Data;

import java.io.Serializable;

@Data
public class SocialUser implements Serializable {
    private String access_token;

    private String remind_in;

    private long expires_in;

    private String uid;

    private String isRealName;
}