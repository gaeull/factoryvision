package webproject.factoryvision.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import webproject.factoryvision.redis.CacheNames;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class CacheConfig {

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory, ResourceLoader resourceLoader) {
        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .disableCachingNullValues()
                .serializeValuesWith(
                        RedisSerializationContext
                                .SerializationPair
                                .fromSerializer(new GenericJackson2JsonRedisSerializer())// value Serializer 변경
                );

        //redisCacheConfigMap은 Redis 캐시의 구성 정보를 담는 맵입니다.
        Map<String, RedisCacheConfiguration> redisCacheConfigMap = new HashMap<>();

        redisCacheConfigMap.put(
                CacheNames.USERBYUSERNAME,
                defaultConfig.entryTtl(Duration.ofHours(4)) //entryTtl()을 호출하여 캐시 항목의 만료 시간(TTL)을 설정합니다.  캐시 수명 4시간
        );

        // ALLUSERS에 대해서만 다른 Serializer 적용
        redisCacheConfigMap.put(
                CacheNames.ALLUSERS,
                defaultConfig.entryTtl(Duration.ofHours(4))
                        .serializeValuesWith( //serializeValuesWith()를 호출하여 값을 직렬화하는 방식을 설정합니다.
                                RedisSerializationContext
                                        .SerializationPair
                                        .fromSerializer(new JdkSerializationRedisSerializer())
                        )
        );

        redisCacheConfigMap.put(
                CacheNames.LOGINUSER,
                defaultConfig.entryTtl(Duration.ofHours(2))
        );

        return RedisCacheManager.builder(connectionFactory)
                .withInitialCacheConfigurations(redisCacheConfigMap)
                .build();
    }

}
