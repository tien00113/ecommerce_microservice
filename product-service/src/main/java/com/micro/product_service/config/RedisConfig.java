package com.micro.product_service.config;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import redis.clients.jedis.JedisPoolConfig;

@Configuration
@EnableCaching
public class RedisConfig {
    @Value("${spring.data.redis.host}")
    private String redisHost;
    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Bean
    public JedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(redisHost);
        // redisStandaloneConfiguration.setPassword("12345678");
        redisStandaloneConfiguration.setPort(redisPort);

        JedisPoolConfig p = new JedisPoolConfig();
        p.setTestWhileIdle(true);
        // p.setMinEvictableIdleTime(Duration.ofMillis(60000));
        p.setMinEvictableIdleDuration(Duration.ofMillis(60000));
        p.setTimeBetweenEvictionRuns(Duration.ofMillis(30000));

        JedisClientConfiguration.JedisClientConfigurationBuilder jedisClientConfiguration = JedisClientConfiguration
                .builder();
        jedisClientConfiguration.usePooling().poolConfig(p);
        jedisClientConfiguration.connectTimeout(Duration.ofMillis(60000));
        jedisClientConfiguration.readTimeout(Duration.ofMillis(60000));
        return new JedisConnectionFactory(redisStandaloneConfiguration, jedisClientConfiguration.build());
    }

    // @Bean
    // public RedisSerializer<Object> redisSerializer() {
    //     return new GenericJackson2JsonRedisSerializer();
    // }

    // @Bean
    // public RedisCacheConfiguration redisCacheConfiguration() {
    //     RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
    //             .entryTtl(Duration.ofMinutes(60))
    //             .serializeKeysWith(
    //                     RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
    //             .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer()));
    //     return configuration;
    // }

    // @Bean
    // public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
    //     RedisCacheManager cacheManager = RedisCacheManager.builder(redisConnectionFactory)
    //             .cacheDefaults(redisCacheConfiguration())
    //             .build();
    //     return cacheManager;
    // }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer(redisObjectMapper()));
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer(redisObjectMapper()));
        template.setConnectionFactory(redisConnectionFactory());
        return template;
    }

    @Bean
    public ObjectMapper redisObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateTimeFormatter.ISO_DATE_TIME));
        module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ISO_DATE_TIME));
        objectMapper.registerModule(module);

        return objectMapper;
    }

    // @Bean
    // public ExecutorService executorService() {
    //     return Executors.newFixedThreadPool(10);
    // }
}
