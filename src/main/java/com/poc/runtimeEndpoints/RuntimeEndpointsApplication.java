package com.poc.runtimeEndpoints;

import groovy.util.ConfigObject;
import groovy.util.ConfigSlurper;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Primary;

import java.net.MalformedURLException;
import java.net.URL;

@SpringBootApplication
@CommonsLog
@ImportResource(value = "classpath:resources.groovy")
public class RuntimeEndpointsApplication {

    public static void main(String[] args) {
        SpringApplication.run(RuntimeEndpointsApplication.class, args);
    }

    @Bean
    @Primary
    public ConfigObject configObject() throws MalformedURLException {
         return new ConfigSlurper().parse(new URL("classpath:application.groovy"));
    }
}
