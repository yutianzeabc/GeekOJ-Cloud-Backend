package cc.geektip.geekoj.api.model.dto.userchat;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class MessageQueryRequest extends PageRequest {
    private Long conversationId;
}
