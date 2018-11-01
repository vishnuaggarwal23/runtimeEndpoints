package com.ttn.elasticsearchAPI.init;

import com.ttn.elasticsearchAPI.co.SearchCO;
import com.ttn.elasticsearchAPI.controller.GenericController;
import com.ttn.elasticsearchAPI.util.ConfigHelper;
import groovy.util.ConfigObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.util.CollectionUtils.isEmpty;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Configuration
public class URLMappingBuilder {

    @Autowired
    public URLMappingBuilder(
            final RequestMappingHandlerMapping requestMappingHandlerMapping,
            final GenericController genericController,
            final ConfigHelper configHelper) {

        final ConfigObject urlMapping = configHelper.getAPIConfig();
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
                        requestMappingHandlerMapping.registerMapping(requestMappingInfo, genericController, GenericController.class.getDeclaredMethod("postRequest", SearchCO.class, HttpServletRequest.class));
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
