package com.example.practice.demo_one.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

	@Value("${spring.redis.host:${spring.data.redis.host:}}")
	private String redisHost;

	@Value("${spring.redis.port:${spring.data.redis.port:6379}}")
	private int redisPort;

	@Value("${spring.redis.password:${spring.data.redis.password:}}")
	private String redisPassword;

	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		RedisStandaloneConfiguration cfg = new RedisStandaloneConfiguration(redisHost, redisPort);
		if (redisPassword != null && !redisPassword.isBlank()) {
			cfg.setPassword(RedisPassword.of(redisPassword));
		}
		return new LettuceConnectionFactory(cfg);
	}

	@Bean
	public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
		GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer();
		RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
				.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer))
				.entryTtl(Duration.ofHours(1));

		return RedisCacheManager.builder(connectionFactory).cacheDefaults(config).build();
	}

	@Bean
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
	    RedisTemplate<String, Object> template = new RedisTemplate<>();
	    template.setConnectionFactory(connectionFactory);
	    
	    // Use the same JSON serializer we used for the cache
	    GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer();
	    
	    template.setKeySerializer(new StringRedisSerializer());
	    template.setValueSerializer(serializer);
	    template.setHashKeySerializer(new StringRedisSerializer());
	    template.setHashValueSerializer(serializer);
	    
	    template.afterPropertiesSet();
	    return template;
	}
}
