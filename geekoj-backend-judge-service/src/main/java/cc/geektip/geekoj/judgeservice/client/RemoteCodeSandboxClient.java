package cc.geektip.geekoj.judgeservice.client;

import cc.geektip.geekoj.api.model.dto.codesandbox.ExecuteCodeRequest;
import cc.geektip.geekoj.api.model.dto.codesandbox.ExecuteCodeResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * @description: 远程代码沙箱OpenFeign客户端接口类
 * @author: Bill Yu
 *
 */

@FeignClient(name = "geekoj-code-sandbox", fallback = RemoteCodeSandboxFallback.class)
public interface RemoteCodeSandboxClient {
    @PostMapping("/executeCode")
    ExecuteCodeResponse executeCode(@RequestHeader("x-service-key") String xServiceKey, @RequestBody ExecuteCodeRequest executeCodeRequest);
}
