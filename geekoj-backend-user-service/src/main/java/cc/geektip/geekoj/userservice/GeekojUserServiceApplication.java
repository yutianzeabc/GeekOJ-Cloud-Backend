package cc.geektip.geekoj.userservice;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("cc.geektip.geekoj.userservice.mapper")
@ComponentScan("cc.geektip.geekoj")
@EnableDubbo(scanBasePackages = "cc.geektip.geekoj.userservice.service")
@EnableScheduling
@EnableDiscoveryClient
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public class GeekojUserServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(GeekojUserServiceApplication.class, args);
    }

}
