package webproject.factoryvision.redis;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedisDao {

    private final RedisTemplate<String, String> redisTemplate;

    /**
     * 값을 저장
     * key : userId
     * value: refreshToken
     * expiredTime: refreshTokenTime
     */
    public void setRefreshToken(String key, String refreshToken, long refreshTokenTime) {
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(refreshToken.getClass()));
        redisTemplate.opsForValue().set(key, refreshToken, refreshTokenTime, TimeUnit.MINUTES);
    }


    /**
     * 키로 값을 조회
     * @param : userId
     * @return 해당 리프레쉬 토큰
     */
    public String getRefreshToken(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     *키로 값을 삭제
     * @param key : userId
     */
    public void deleteRefreshToken(String key) {
        Boolean delete = redisTemplate.delete(key);

        // 2/25 log
        if (delete != null && delete) {
            log.info("Successfully deleted refresh token for key: {}", key);
        } else {
            log.warn("Failed to delete refresh token for key: {}", key);
        }

    }
    
    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    public void setBlackList(String accessToken, String msg, Long minutes) {
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(msg.getClass()));
        redisTemplate.opsForValue().set(accessToken, msg, minutes, TimeUnit.MINUTES);
        log.info("AccessToken을 블랙리스트에 추가했습니다. AccessToken: {}, Message: {}, 만료 시간(분): {}", accessToken, msg, minutes);
    }

    public String getBlackList(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public boolean deleteBlackList(String key) {
        return Boolean.TRUE.equals(redisTemplate.delete(key));
    }

    /***
     * 레디스에 있는 모든 데이터를 삭제
     */
    public void flushAll(){
        redisTemplate.getConnectionFactory().getConnection().serverCommands().flushAll();
    }
}
