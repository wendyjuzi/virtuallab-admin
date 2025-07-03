package com.edu.virtuallab;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@MapperScan({"com.edu.virtuallab.**.dao", "com.edu.virtuallab.**.mapper"})

public class VirtuallabAdminApplication {
    public static void main(String[] args) {
        SpringApplication.run(VirtuallabAdminApplication.class, args);
    }
} 