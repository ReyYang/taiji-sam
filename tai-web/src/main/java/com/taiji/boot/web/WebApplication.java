package com.taiji.boot.web;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Demo WebApplication
 *
 * @author ydy
 * @date 2020/1/4 21:10
 */
@Slf4j
@SpringBootApplication(scanBasePackages = {"com.taiji"})
@ComponentScan(basePackages = {"com.taiji"})
public class WebApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }
}
