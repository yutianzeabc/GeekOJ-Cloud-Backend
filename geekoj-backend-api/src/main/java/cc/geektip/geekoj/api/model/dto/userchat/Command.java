package cc.geektip.geekoj.api.model.dto.userchat;

import cc.geektip.geekoj.api.model.entity.userchat.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Command {
    private Integer code;
    private Long uid;
    private Long conversationId;
    private ChatMessage chatMessage;
}
