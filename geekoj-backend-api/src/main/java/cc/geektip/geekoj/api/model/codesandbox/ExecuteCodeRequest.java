package cc.geektip.geekoj.api.model.codesandbox;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
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
public class ExecuteCodeRequest implements Serializable {
    private List<String> inputList;
    private String code;
    private String language;
}
