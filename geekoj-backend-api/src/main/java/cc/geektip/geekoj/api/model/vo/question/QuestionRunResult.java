package cc.geektip.geekoj.api.model.vo.question;

import lombok.Data;

import java.io.Serializable;

@Data
public class QuestionRunResult implements Serializable {
    /**
     * 执行状态
     */
    private Integer code;
    /**
     * 输入
     */
    private String input;
    /**
     * 执行结果
     */
    private String output;

}
