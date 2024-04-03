package cc.geektip.geekoj.userservice.service.impl;

import cc.geektip.geekoj.api.model.entity.user.UserTagCategory;
import cc.geektip.geekoj.api.service.user.UserTagCategoryService;
import cc.geektip.geekoj.userservice.mapper.UserTagCategoryMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @description: 用户标签分类服务实现类
 * @author: Fish
 *
 */
@Slf4j
@DubboService
public class UserTagCategoryServiceImpl extends ServiceImpl<UserTagCategoryMapper, UserTagCategory> implements UserTagCategoryService {

}
