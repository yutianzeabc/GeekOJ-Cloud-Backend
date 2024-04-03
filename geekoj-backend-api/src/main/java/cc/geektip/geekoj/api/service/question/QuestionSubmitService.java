package cc.geektip.geekoj.api.service.question;

import cc.geektip.geekoj.api.model.dto.question.QuestionSubmitAddRequest;
import cc.geektip.geekoj.api.model.dto.question.QuestionSubmitQueryRequest;
import cc.geektip.geekoj.api.model.entity.question.QuestionSubmit;
import cc.geektip.geekoj.api.model.vo.question.QuestionSubmitSummaryVo;
import cc.geektip.geekoj.api.model.vo.question.QuestionSubmitVo;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Fish
* @description 针对表【question_submit(题目提交)】的数据库操作Service
*/
public interface QuestionSubmitService extends IService<QuestionSubmit> {

    QuestionSubmit doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest);

    Page<QuestionSubmitVo> listQuestionSubmitVoByPage(QuestionSubmitQueryRequest questionSubmitQueryRequest);

    QuestionSubmitSummaryVo getSubmitSummary();
}
