package cc.geektip.geekoj.judgeservice.client;

import cc.geektip.geekoj.api.model.dto.codesandbox.ExecuteCodeRequest;
import cc.geektip.geekoj.api.model.dto.codesandbox.ExecuteCodeResponse;
import cc.geektip.geekoj.common.common.AppHttpCodeEnum;
import cc.geektip.geekoj.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;

/**
 * @description: 远程代码沙箱OpenFeign客户端Fallback
 * @author: Bill Yu
 */
@Slf4j
public class RemoteCodeSandboxFallback implements RemoteCodeSandboxClient {
    @Override
    public ExecuteCodeResponse executeCode(String xServiceKey, ExecuteCodeRequest executeCodeRequest) {
        log.error("远程代码沙箱调用失败！");
        throw new BusinessException(AppHttpCodeEnum.SUBMIT_ERROR);
    }
}
