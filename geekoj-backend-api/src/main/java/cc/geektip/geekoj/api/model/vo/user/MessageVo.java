package cc.geektip.geekoj.api.model.vo.user;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class MessageVo implements Serializable {
    private Long id;
    private Long conversationId;
    private Long fromUid;
    private String fromUsername;
    private String avatar;
    private String content;
    private Date createTime;
}
