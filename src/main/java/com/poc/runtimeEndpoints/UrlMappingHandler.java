package com.poc.runtimeEndpoints;

/*
Created by vishnu on 12/10/18 9:53 AM
*/

import groovy.util.ConfigObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Map;

@Configuration
public class UrlMappingHandler {

    @Autowired
    public UrlMappingHandler(
            RequestMappingHandlerMapping requestMappingHandlerMapping,
            GenericController genericController,
            ConfigObject configObject) {

        ConfigObject urlMapping = (ConfigObject) configObject.get("urlMapping");
        if (!CollectionUtils.isEmpty(urlMapping)) {
            ConfigObject post = (ConfigObject) urlMapping.get("post");
            ConfigObject get = (ConfigObject) urlMapping.get("get");
            if (!CollectionUtils.isEmpty(post)) {
                post.keySet().forEach(it -> {
                    try {
                        Map value = (Map) post.get(it);
                        RequestMappingInfo requestMappingInfo = RequestMappingInfo
                                .paths(value.get("url").toString())
                                .methods(RequestMethod.POST)
                                .produces(MediaType.APPLICATION_JSON_UTF8_VALUE)
                                .build();
                        requestMappingHandlerMapping.registerMapping(requestMappingInfo, genericController, GenericController.class.getDeclaredMethod("postRequest", String.class));
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                });
            }
            if (!CollectionUtils.isEmpty(get)) {
                get.keySet().forEach(it -> {
                    try {
                        Map value = (Map) get.get(it);
                        RequestMappingInfo requestMappingInfo = RequestMappingInfo
                                .paths(value.get("url").toString())
                                .methods(RequestMethod.GET)
                                .produces(MediaType.APPLICATION_JSON_UTF8_VALUE)
                                .build();
                        requestMappingHandlerMapping.registerMapping(requestMappingInfo, genericController, GenericController.class.getDeclaredMethod("getRequest"));
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                });
            }
        }
    }
}
