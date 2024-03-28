package cc.geektip.geekoj.api.model.dto.question;

import jakarta.validation.constraints.NotBlank;

/**
 * @description:
 * @author: Fish
 * @date: 2024/3/28
 */
public class QuestionRunRequest {
    @NotBlank
    private String code;
    private String input;
    @NotBlank
    private String language;
}
