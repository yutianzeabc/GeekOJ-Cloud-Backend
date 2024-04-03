package cc.geektip.geekoj.api.model.dto.question;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;


@Data
public class QuestionRunRequest implements Serializable {
    private String code;
    private String input;
    @NotBlank
    private String language;
}
