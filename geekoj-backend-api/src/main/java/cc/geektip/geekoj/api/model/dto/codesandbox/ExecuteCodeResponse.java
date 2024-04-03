package cc.geektip.geekoj.api.model.dto.codesandbox;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @description: 代码沙箱响应
 * @author: Fish
 * @date: 2024/2/28
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExecuteCodeResponse {
    /**
     * 执行信息
     */
    private String msg;
    /**
     * 执行状态
     */
    private Integer code;
    /**
     * 执行结果
     */
    private List<ExecuteResult> results;
}
