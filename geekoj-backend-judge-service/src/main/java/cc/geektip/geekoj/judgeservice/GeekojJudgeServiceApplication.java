package cc.geektip.geekoj.judgeservice;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan("cc.geektip.geekoj")
@EnableDubbo
@EnableFeignClients
@EnableScheduling
@EnableDiscoveryClient
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public class GeekojJudgeServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GeekojJudgeServiceApplication.class, args);
    }

}
