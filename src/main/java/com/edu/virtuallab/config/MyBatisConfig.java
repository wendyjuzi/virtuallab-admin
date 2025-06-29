package com.edu.virtuallab.config;

import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.edu.virtuallab.common.enums.NotificationType;
import com.edu.virtuallab.common.handler.EnumValueTypeHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyBatisConfig {

    @Bean
    public ConfigurationCustomizer mybatisConfigurationCustomizer() {
        return configuration -> {
            configuration.getTypeHandlerRegistry().register(
                    NotificationType.class,
                    new EnumValueTypeHandler()
            );
        };
    }
}