package cc.geektip.geekoj.judgeservice.judge.codesandbox;

import cc.geektip.geekoj.judgeservice.judge.codesandbox.impl.ExampleCodeSandbox;
import cc.geektip.geekoj.judgeservice.judge.codesandbox.impl.RemoteCodeSandbox;
import cc.geektip.geekoj.judgeservice.judge.codesandbox.impl.ThirdPartyCodeSandbox;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: 代码沙箱注册中心，用于获取代码沙箱实例对象，用了单例模式，保证代码沙箱实例唯一性
 * @author: Fish
 * @date: 2024/2/28
 */
@Component
public class CodeSandboxRegistry {

    Map<String, CodeSandbox> codeSandboxMap = new ConcurrentHashMap<>();
    @Resource
    ExampleCodeSandbox exampleCodeSandbox;
    @Resource
    RemoteCodeSandbox remoteCodeSandbox;
    @Resource
    ThirdPartyCodeSandbox thirdPartyCodeSandbox;

    @PostConstruct
    public void init() {
        codeSandboxMap.put(CodeSandboxEnum.EXAMPLE.getValue(), exampleCodeSandbox);
        codeSandboxMap.put(CodeSandboxEnum.REMOTE.getValue(), remoteCodeSandbox);
        codeSandboxMap.put(CodeSandboxEnum.THIRD_PARTY.getValue(), thirdPartyCodeSandbox);
    }

    public CodeSandbox getInstance(String type) {
        if (codeSandboxMap.containsKey(type)) {
            return codeSandboxMap.get(type);
        } else {
            return codeSandboxMap.get(CodeSandboxEnum.EXAMPLE.getValue());
        }
    }

}
