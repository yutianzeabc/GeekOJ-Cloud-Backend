package cc.geektip.geekoj.userservice.mapper;

import cc.geektip.geekoj.api.model.entity.user.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 用户数据库操作
 *
 */
public interface UserMapper extends BaseMapper<User> {
    List<User> getRandomRecommend(@Param("uid") Long uid, @Param("count") int count);

    List<User> getRandom(@Param("count") int count);

    void incrFollow(@Param("uid") Long uid);

    void decrFollow(@Param("uid") Long uid);

    void incrFans(@Param("uid") Long uid);

    void decrFans(@Param("uid") Long uid);
}




