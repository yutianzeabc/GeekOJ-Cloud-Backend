package cc.geektip.geekoj.userservice;

import cc.geektip.geekoj.userservice.utils.MailUtil;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class GeekojUserServiceApplicationTests {

    @Resource
    MailUtil mailUtil;

    @Test
    void contextLoads() {
    }

    @Test
    void testSendMail() {
        mailUtil.sendMail("***REMOVED***", "123456");
    }

}
