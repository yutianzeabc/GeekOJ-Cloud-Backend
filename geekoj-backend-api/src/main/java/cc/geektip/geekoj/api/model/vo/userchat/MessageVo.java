package cc.geektip.geekoj.api.model.vo.userchat;

import lombok.Data;

import java.util.Date;

@Data
public class MessageVo {
    private Long id;
    private Long conversationId;
    private Long fromUid;
    private String fromUsername;
    private String avatar;
    private String content;
    private Date createTime;
}
