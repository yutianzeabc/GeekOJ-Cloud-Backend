package cc.geektip.geekoj.api.model.dto.mq;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class JudgeMQMsg implements Serializable {
    private Long questionSubmitId;
}