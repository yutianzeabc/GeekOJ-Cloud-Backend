package cc.geektip.geekoj.common.exception;

import cc.geektip.geekoj.common.common.AppHttpCodeEnum;

/**
 * 异常抛出工具类
 *
 */
public class ThrowUtils {

    /**
     * 条件成立则抛异常
     *
     * @param condition
     * @param runtimeException
     */
    public static void throwIf(boolean condition, RuntimeException runtimeException) {
        if (condition) {
            throw runtimeException;
        }
    }

    /**
     * 条件成立则抛异常
     *
     * @param condition
     * @param appHttpCodeEnum
     */
    public static void throwIf(boolean condition, AppHttpCodeEnum appHttpCodeEnum) {
        throwIf(condition, new BusinessException(appHttpCodeEnum));
    }

    /**
     * 条件成立则抛异常
     * @param condition
     * @param appHttpCodeEnum
     * @param msg
     */
    public static void throwIf(boolean condition, AppHttpCodeEnum appHttpCodeEnum, String msg) {
        throwIf(condition, new BusinessException(appHttpCodeEnum, msg));
    }
}
