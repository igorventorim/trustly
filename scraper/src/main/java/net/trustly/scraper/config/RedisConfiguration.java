package net.trustly.scraper.config;

import io.lettuce.core.ClientOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfiguration {
    private final String url;

    private final int port;

    private final String password;

    public RedisConfiguration(@Value("${spring.redis.host}") String url, @Value("${spring.redis.port}") int port, @Value("${spring.redis.password}") String password) {
        this.url = url;
        this.port = port;
        this.password = password;
    }

    @Bean
    public RedisStandaloneConfiguration redisStandaloneConfiguration() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(this.url, this.port);
        redisStandaloneConfiguration.setPassword(this.password);
        return redisStandaloneConfiguration;
    }

    @Bean
    public ClientOptions clientOptions() {
        return ClientOptions.builder()
                .disconnectedBehavior(ClientOptions.DisconnectedBehavior.REJECT_COMMANDS)
                .autoReconnect(true)
                .build();
    }

    @Bean
    public RedisConnectionFactory connectionFactory(RedisStandaloneConfiguration redisStandaloneConfiguration) {
        LettuceClientConfiguration configuration = LettuceClientConfiguration.builder().clientOptions(clientOptions()).build();
        return (RedisConnectionFactory)new LettuceConnectionFactory(redisStandaloneConfiguration, configuration);
    }

    @Bean
    @ConditionalOnMissingBean(name = {"redisTemplate"})
    @Primary
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }
}
