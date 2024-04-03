package cc.geektip.geekoj.questionservice.mapper;

import cc.geektip.geekoj.api.model.entity.question.Question;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * 题目数据库操作
 *
 */
public interface QuestionMapper extends BaseMapper<Question> {
    void incrSubmitCount(@Param("id") Long questionId);

    void incrAcceptedCount(@Param("id") Long questionId);
}




