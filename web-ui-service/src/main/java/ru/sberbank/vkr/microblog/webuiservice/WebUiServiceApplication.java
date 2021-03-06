package ru.sberbank.vkr.microblog.webuiservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class WebUiServiceApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(WebUiServiceApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(WebUiServiceApplication.class);
    }
}
