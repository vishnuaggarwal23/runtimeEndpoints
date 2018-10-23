package com.poc.runtimeEndpoints;

/*
Created by vishnu on 12/10/18 9:53 AM
*/

import groovy.util.ConfigObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.util.CollectionUtils.isEmpty;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Configuration
public class UrlMappingHandler {

    @Autowired
    public UrlMappingHandler(
            final RequestMappingHandlerMapping requestMappingHandlerMapping,
            final GenericController genericController,
            final ConfigObject configObject) {

        final ConfigObject urlMapping = (ConfigObject) configObject.get("urlMapping");
        if (!isEmpty(urlMapping)) {
            final ConfigObject post = (ConfigObject) urlMapping.get("post");
            final ConfigObject get = (ConfigObject) urlMapping.get("get");
            if (!isEmpty(post) && !isEmpty(post.keySet())) {
                post.keySet().forEach(it -> {
                    try {
                        Map value = (Map) post.get(it);
                        RequestMappingInfo requestMappingInfo = RequestMappingInfo
                                .paths(value.get("uri").toString())
                                .methods(POST)
                                .produces(APPLICATION_JSON_UTF8_VALUE)
                                .consumes(APPLICATION_JSON_UTF8_VALUE)
                                .build();
                        requestMappingHandlerMapping.registerMapping(requestMappingInfo, genericController, GenericController.class.getDeclaredMethod("postRequest", String.class));
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                });
            }
            if (!isEmpty(get) && !isEmpty(get.keySet())) {
                get.keySet().forEach(it -> {
                    try {
                        Map value = (Map) get.get(it);
                        RequestMappingInfo requestMappingInfo = RequestMappingInfo
                                .paths(value.get("uri").toString())
                                .methods(RequestMethod.GET)
                                .produces(APPLICATION_JSON_UTF8_VALUE)
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
