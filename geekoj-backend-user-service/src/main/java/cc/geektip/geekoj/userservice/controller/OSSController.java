package cc.geektip.geekoj.userservice.controller;

import cc.geektip.geekoj.common.common.AppHttpCodeEnum;
import cc.geektip.geekoj.common.common.R;
import cc.geektip.geekoj.common.exception.BusinessException;
import cc.geektip.geekoj.userservice.config.OSSProperties;
import com.qiniu.util.Auth;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/oss")
public class OSSController {
    @Resource
    private OSSProperties ossProperties;

    @GetMapping("/policy")
    public R<Map<String, Object>> policy() {
        Map<String, Object> result = new HashMap<>();
        try {
            // 创建认证
            Auth auth = Auth.create(ossProperties.getAccessKey(), ossProperties.getSecretKey());
            // 生成上传凭证
            String upToken = auth.uploadToken(ossProperties.getBucket());
            result.put("token", upToken);
            result.put("domain", ossProperties.getDomain());

            // 上传目录
            String dir = "oj/" + new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            result.put("dir", dir);

            result.put("success", 1);
            return R.ok(result);
        } catch (Exception e) {
            throw new BusinessException(AppHttpCodeEnum.INTERNAL_SERVER_ERROR, "获取凭证失败，" + e.getMessage());
        }
    }
}
