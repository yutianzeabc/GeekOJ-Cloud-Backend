package cc.geektip.geekoj.api.model.codesandbox;

import lombok.Data;

import java.io.Serializable;

/**
 * 判题信息
 */
@Data
public class JudgeInfo implements Serializable {

    /**
     * 程序执行信息
     */
    private String message;

    /**
     * 消耗内存
     */
    private Long memory;

    /**
     * 消耗时间（KB）
     */
    private Long time;
}

