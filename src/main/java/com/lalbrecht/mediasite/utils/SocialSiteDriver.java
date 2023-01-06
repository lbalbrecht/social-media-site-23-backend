package com.lalbrecht.mediasite.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class SocialSiteDriver {
    public static void main(String[] args) {
        SpringApplication.run(SocialSiteDriver.class, args);
        log.info("Springboot and lombok application started successfully.");
    }
}
