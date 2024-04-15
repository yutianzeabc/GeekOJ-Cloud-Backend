package cc.geektip.geekoj.judgeservice.judge.strategy;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: 判题策略注册中心，用于获取判题策略实例对象，用了单例模式，保证判题策略实例唯一性
 * @author: Bill Yu
 *
 */
@Component
public class JudgeStrategyRegistry {

    Map<String, JudgeStrategy> judgeStrategyMap = new ConcurrentHashMap<>();
    @Resource
    JudgeStrategy defaultJudgeStrategy;
    @Resource
    JudgeStrategy javaLanguageJudgeStrategy;

    @PostConstruct
    public void init() {
        judgeStrategyMap.put(JudgeStrategyEnum.DEFAULT.getValue(), defaultJudgeStrategy);
        // 暂不注册独立的 Java 语言判题策略
        // judgeStrategyMap.put(JudgeStrategyEnum.JAVA.getValue(), javaLanguageJudgeStrategy);
    }

    public JudgeStrategy getInstance(String type) {
        if (judgeStrategyMap.containsKey(type)) {
            return judgeStrategyMap.get(type);
        } else {
            return judgeStrategyMap.get(JudgeStrategyEnum.DEFAULT.getValue());
        }
    }

}
