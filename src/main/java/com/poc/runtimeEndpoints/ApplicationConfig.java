package com.poc.runtimeEndpoints;

import groovy.util.ConfigObject;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.springframework.util.CollectionUtils.isEmpty;

/*
Created by vishnu on 15/10/18 1:30 PM
*/

@Configuration
@CommonsLog
public class ApplicationConfig implements WebMvcConfigurer {

    private final UrlInterceptor urlInterceptor;
    private final ConfigObject configObject;

    @Autowired
    public ApplicationConfig(
            final UrlInterceptor urlInterceptor,
            final ConfigObject configObject) {
        this.urlInterceptor = urlInterceptor;
        this.configObject = configObject;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(urlInterceptor).addPathPatterns(getUriPaths());
    }

    private List<String> getUriPaths() {
        List<String> paths = new ArrayList<String>();
        final ConfigObject urlMapping = (ConfigObject) configObject.get("urlMapping");
        if (!isEmpty(urlMapping)) {
            paths.addAll(addToPaths((ConfigObject) urlMapping.get("post")));
            paths.addAll(addToPaths((ConfigObject) urlMapping.get("get")));
        }
        return paths;
    }

    private List<String> addToPaths(ConfigObject configObject) {
        List<String> paths = new ArrayList<String>();
        if (!isEmpty(configObject) && !isEmpty(configObject.keySet())) {
            configObject.keySet().forEach(it -> {
                Map value = (Map) configObject.get(it);
                if (!isEmpty(value)) {
                    paths.add((String) value.get("uri"));
                }
            });
        }
        return paths;
    }
}
