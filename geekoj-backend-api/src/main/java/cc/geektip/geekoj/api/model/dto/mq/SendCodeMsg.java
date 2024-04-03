package cc.geektip.geekoj.api.model.dto.mq;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class SendCodeMsg implements Serializable {
    private String type;
    private String dest;
    private String codeNum;
}