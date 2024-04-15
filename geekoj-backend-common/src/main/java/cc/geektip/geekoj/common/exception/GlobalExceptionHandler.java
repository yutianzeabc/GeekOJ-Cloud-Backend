package cc.geektip.geekoj.common.exception;

import cc.geektip.geekoj.common.common.AppHttpCodeEnum;
import cc.geektip.geekoj.common.common.R;
import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotRoleException;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 全局异常处理器
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public R businessExceptionHandler(BusinessException e) {
        log.error("出现了异常！{}", e.getMessage());
        return R.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(NotLoginException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public R notLoginExceptionHandler(NotLoginException e) {
        log.error("出现了异常！{}", e.getMessage());
        return R.error(AppHttpCodeEnum.NOT_LOGIN);
    }

    @ExceptionHandler(NotRoleException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public R notLoginExceptionHandler(NotRoleException e) {
        log.error("出现了异常！{}", e.getMessage());
        return R.error(AppHttpCodeEnum.NO_AUTH);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R constraintViolationExceptionHandler(ConstraintViolationException e) {
        log.error("出现了异常！{}", e.getMessage());
        return R.error(AppHttpCodeEnum.PARAMS_ERROR.getCode(), e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        log.error("出现了异常！{}", e.getMessage());
        return R.error(AppHttpCodeEnum.PARAMS_ERROR.getCode(), e.getMessage());
    }

    @ExceptionHandler(BlockException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    public R rateLimitExceptionHandler(BlockException e) {
        log.error("出现了异常！{}", e.getMessage());
        return R.error(AppHttpCodeEnum.RATE_LIMIT);
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public R fallBackExceptionHandler(Exception e) {
        log.error("出现了异常！{}", e.getMessage());
        return R.error(AppHttpCodeEnum.INTERNAL_SERVER_ERROR.getCode(), e.getMessage());
    }
}
