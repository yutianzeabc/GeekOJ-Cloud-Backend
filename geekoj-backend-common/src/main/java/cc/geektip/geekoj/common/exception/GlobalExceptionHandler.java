package cc.geektip.geekoj.common.exception;

import cc.geektip.geekoj.common.common.AppHttpCodeEnum;
import cc.geektip.geekoj.common.common.R;
import cn.dev33.satoken.exception.SaTokenException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;



@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public R systemExceptionHandler(BusinessException e){
        //打印异常信息
        log.error("出现了异常！{}", e.getMessage());
        //从异常对象中获取信息，封装成ResponseResult后返回
        return R.error(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(SaTokenException.class)
    @ResponseBody
    public R notLoginExceptionHandler(SaTokenException e){
        //打印异常信息
        log.error("出现了异常！{}", e.getMessage());
        //从异常对象中获取信息，封装成ResponseResult后返回
        return R.error(AppHttpCodeEnum.NOT_LOGIN.getCode(), e.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public R validationExceptionHandler(ConstraintViolationException e){
        //打印异常信息
        log.error("出现了异常！{}", e.getMessage());
        //从异常对象中获取信息，封装成ResponseResult后返回
        return R.error(AppHttpCodeEnum.PARAMS_ERROR.getCode(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public R exceptionHandler(Exception e){
        //打印异常信息
        log.error("出现了异常！{}", e.getMessage());
        //从异常对象中获取信息，封装成ResponseResult后返回
        return R.error(AppHttpCodeEnum.INTERNAL_SERVER_ERROR.getCode(), e.getMessage());
    }
}
