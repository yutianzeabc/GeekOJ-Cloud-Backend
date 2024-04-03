package cc.geektip.geekoj.userservice.utils;

import cc.geektip.geekoj.common.common.AppHttpCodeEnum;
import cc.geektip.geekoj.common.exception.BusinessException;
import cc.geektip.geekoj.common.utils.ObjectMapperUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

import static cc.geektip.geekoj.common.utils.ObjectMapperUtils.MAPPER;

/**
 * Redis工具类
 */

@Component
public class RedisUtils {
    @Resource
    private StringRedisTemplate stringRedisTemplate;


    public <T> void rightPushAllAsString(String cacheKey, List<T> list){
        stringRedisTemplate.opsForList().rightPushAll(cacheKey,
                list.stream().map(ObjectMapperUtils::writeValueAsString).collect(Collectors.toList()));
    }

    public <T> List<T> readList(String cacheKey, Class<T> clazz) {
        return stringRedisTemplate.opsForList().range(cacheKey, 0, -1)
                .stream().map(itemStr -> {
                    try {
                        return MAPPER.readValue(itemStr, clazz);
                    } catch (JsonProcessingException e) {
                        throw new BusinessException(AppHttpCodeEnum.INTERNAL_SERVER_ERROR);
                    }
                }).collect(Collectors.toList());
    }
}
