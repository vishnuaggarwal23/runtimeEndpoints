package com.ttn.elasticsearchAPI.init;

import com.ttn.elasticsearchAPI.interceptor.RequestInterceptor;
import com.ttn.elasticsearchAPI.util.ConfigHelper;
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
public class ApplicationConfigurer implements WebMvcConfigurer {

    private final RequestInterceptor requestInterceptor;
    private final ConfigHelper configHelper;

    @Autowired
    public ApplicationConfigurer(
            final RequestInterceptor requestInterceptor,
            final ConfigHelper configHelper) {
        this.requestInterceptor = requestInterceptor;
        this.configHelper = configHelper;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(requestInterceptor).addPathPatterns(getUriPaths());
    }



    private List<String> getUriPaths() {
        List<String> paths = new ArrayList<String>();
        final ConfigObject urlMapping = configHelper.getAPIConfig();
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
