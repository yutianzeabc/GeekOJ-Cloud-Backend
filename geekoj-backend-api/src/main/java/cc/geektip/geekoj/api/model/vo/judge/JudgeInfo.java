package cc.geektip.geekoj.api.model.vo.judge;

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
     * 通过用例数
     */
    private Integer pass;
    /**
     * 总用例数
     */
    private Integer total;
    /**
     * 消耗内存
     */
    private Long memory;
    /**
     * 消耗时间（KB）
     */
    private Long time;

    //状态
    private String status;

    private String input;
    private String output;
    private String expectedOutput;
}
