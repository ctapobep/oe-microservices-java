package com.oecontrib.microservices;

import org.springframework.boot.SpringApplication;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

public class AppStarter extends WebMvcConfigurerAdapter {
    public static void main(String[] args) {
        SpringApplication.run(new Object[]{new ClassPathResource("/appContext.xml")}, args);
    }
}
