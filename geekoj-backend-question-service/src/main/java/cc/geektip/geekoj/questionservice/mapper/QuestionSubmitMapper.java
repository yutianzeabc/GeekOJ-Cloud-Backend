package cc.geektip.geekoj.questionservice.mapper;

import cc.geektip.geekoj.api.model.entity.question.QuestionSubmit;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 题目提交数据库操作
 *
 */
public interface QuestionSubmitMapper extends BaseMapper<QuestionSubmit> {
    Long getPassCount(@Param("userId") Long userId, @Param("ids") List<Long> ids);
}




