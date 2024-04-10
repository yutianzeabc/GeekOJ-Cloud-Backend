package cc.geektip.geekoj.api.service.user;

import cc.geektip.geekoj.api.model.entity.user.Follow;
import cc.geektip.geekoj.api.model.vo.user.FollowVo;
import cc.geektip.geekoj.api.model.vo.user.UserInfoVo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author Bill Yu
* @description 针对表【follow】的数据库操作Service
*/
public interface FollowService extends IService<Follow> {

    void follow(Long uid);

    boolean isFollow(Long uid, Long followId);

    List<Long> getFollowIdsOfCurrent();

    List<FollowVo> getFollowsOfCurrent();

    List<Long> getFollowIdsByUid(Long uid);

    List<UserInfoVo> getFollowsByUid(Long uid);

    List<Long> getFanIdsOfCurrent();

    List<FollowVo> getFansOfCurrent();

    List<Long> getFanIdsByUid(Long uid);

    List<UserInfoVo> getFansByUid(Long uid);

    void removeByUidPair(Long uid, Long followUid);
}
