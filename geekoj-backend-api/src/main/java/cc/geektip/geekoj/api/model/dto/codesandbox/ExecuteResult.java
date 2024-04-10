package cc.geektip.geekoj.api.model.dto.codesandbox;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description: 代码沙箱响应
 * @author: Bill Yu
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExecuteResult {
    /**
     * 退出码
     */
    private Long exitValue;
    /**
     * 正常信息
     */
    private String output;
    /**
     * 错误信息
     */
    private String errorOutput;
    /**
     * 运行时间
     */
    private Long time;
    /**
     * 消耗内存
     */
    private Long memory;
    /**
     * 是否超时
     */
    private boolean isTimeout;
}
