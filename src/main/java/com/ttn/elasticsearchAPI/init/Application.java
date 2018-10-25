package com.ttn.elasticsearchAPI.init;

import groovy.util.ConfigObject;
import groovy.util.ConfigSlurper;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Primary;
import org.springframework.web.context.request.RequestContextListener;

import java.net.MalformedURLException;
import java.net.URL;

@SpringBootApplication
@CommonsLog
@ComponentScan("com.ttn.elasticsearchAPI")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public RequestContextListener requestContextListener(){
        return new RequestContextListener();
    }

}
