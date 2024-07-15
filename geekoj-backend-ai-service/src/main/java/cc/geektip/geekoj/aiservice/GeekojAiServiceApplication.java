package cc.geektip.geekoj.aiservice;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication(scanBasePackages = "cc.geektip.geekoj")
@ComponentScan("cc.geektip.geekoj")
@EnableDubbo(scanBasePackages = "cc.geektip.geekoj.aiservice.service")
@EnableDiscoveryClient
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public class GeekojAiServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GeekojAiServiceApplication.class, args);
    }

}
