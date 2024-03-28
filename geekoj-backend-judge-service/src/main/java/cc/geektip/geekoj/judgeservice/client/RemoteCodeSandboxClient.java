package cc.geektip.geekoj.judgeservice.client;

import cc.geektip.geekoj.api.codesandbox.dto.ExecuteCodeRequest;
import cc.geektip.geekoj.api.codesandbox.dto.ExecuteCodeResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

/**
 * @description: 远程代码沙箱OpenFeign客户端接口类
 * @author: Fish
 * @date: 2024/3/13
 */

@FeignClient(name = "remoteCodeSandboxService", url = "${code-sandbox.remote.endpoint}")
public interface RemoteCodeSandboxClient {
    @PostMapping("/executeCode")
    ExecuteCodeResponse executeCode(@RequestHeader("x-service-key") String xServiceKey, @RequestBody ExecuteCodeRequest executeCodeRequest);
}
