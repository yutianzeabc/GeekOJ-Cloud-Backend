package cc.geektip.geekoj.api.model.vo.user;

import lombok.Data;

import java.io.Serializable;

@Data
public class GiteeUser implements Serializable {
    private String id;
    private String name;
    private String avatar_url;
}
