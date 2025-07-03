package com.edu.virtuallab.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
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
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        // 添加分页插件
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }

}