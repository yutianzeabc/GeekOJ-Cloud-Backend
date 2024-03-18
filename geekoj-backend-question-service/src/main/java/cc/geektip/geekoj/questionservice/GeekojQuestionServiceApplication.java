package cc.geektip.geekoj.questionservice;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("cc.geektip.geekoj.questionservice.mapper")
@ComponentScan("cc.geektip.geekoj")
@EnableDubbo
@EnableScheduling
@EnableDiscoveryClient
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public class GeekojQuestionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GeekojQuestionServiceApplication.class, args);
    }

}
