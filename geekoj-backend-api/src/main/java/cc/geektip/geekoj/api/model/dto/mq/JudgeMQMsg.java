package cc.geektip.geekoj.api.model.dto.mq;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;


@Data
@AllArgsConstructor
public class JudgeMQMsg implements Serializable {
    private Long questionSubmitId;
}