package cc.geektip.geekoj.api.model.dto.codesandbox;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @description: 代码沙箱请求
 * @author: Fish
 * @date: 2024/2/28
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExecuteCodeRequest {
    @NotEmpty(message = "输入列表不能为空")
    private List<String> inputList;
    @NotBlank(message = "代码不能为空")
    private String code;
    @NotBlank(message = "语言不能为空")
    private String language;
}
