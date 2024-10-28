package com.mcnc.payroll.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.mcnc.payroll.config.cache.LimitedCacheManager;
import com.mcnc.payroll.serializer.MDataRedisSerializer;

@Configuration
@EnableCaching
public class RedisCacheConfig {

	@Value("${spring.cache.redis.time-to-live}")
	private int timeToLive;

	@Value("${spring.cache.redis.max-keys}")
	private int maxKeys;

	@Bean
	RedisConnectionFactory redisConnectionFactory() {
		return new LettuceConnectionFactory();
	}

	@Bean
	CacheManager cacheManager() {
		RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
				.entryTtl(Duration.ofMinutes(timeToLive)) // Set TTL as needed
				.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
				.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new MDataRedisSerializer()))
				.disableCachingNullValues(); // Disable caching of null values, if necessary
		RedisCacheManager redisCacheManager = RedisCacheManager.builder(redisConnectionFactory()).cacheDefaults(redisCacheConfiguration)
				.transactionAware().build();
				
		return new LimitedCacheManager(redisCacheManager, maxKeys);

	}
}
