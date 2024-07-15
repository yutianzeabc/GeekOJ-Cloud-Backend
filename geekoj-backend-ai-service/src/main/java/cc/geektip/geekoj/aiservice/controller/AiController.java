package cc.geektip.geekoj.aiservice.controller;

import cc.geektip.geekoj.api.model.dto.ai.AiAnalyseRequest;
import cc.geektip.geekoj.api.model.dto.ai.AiAnalyseResponse;
import cc.geektip.geekoj.api.service.ai.AiService;
import cc.geektip.geekoj.common.common.R;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: AI 控制器
 * @author: Bill Yu
 */
@RestController
@RequestMapping("/")
public class AiController {

    @Resource
    private AiService aiService;

    @PostMapping("/analyse")
    public R<AiAnalyseResponse> analyse(@RequestBody @NotNull @Valid AiAnalyseRequest aiAnalyseRequest) {
        AiAnalyseResponse aiAnalyseResponse = aiService.doAnalyse(aiAnalyseRequest);
        return R.ok(aiAnalyseResponse);
    }

}
