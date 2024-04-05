package cc.geektip.geekoj.common.exception;

import cc.geektip.geekoj.common.common.AppHttpCodeEnum;
import lombok.Getter;

/**
 * 业务异常类
 */
@Getter
public class BusinessException extends RuntimeException {

    /**
     * 错误码
     */
    private final int code;

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(AppHttpCodeEnum httpCodeEnum) {
        super(httpCodeEnum.getMsg());
        this.code = httpCodeEnum.getCode();
    }

    public BusinessException(AppHttpCodeEnum httpCodeEnum, String msg) {
        super(httpCodeEnum.getMsg() + ": " + msg);
        this.code = httpCodeEnum.getCode();
    }
}
