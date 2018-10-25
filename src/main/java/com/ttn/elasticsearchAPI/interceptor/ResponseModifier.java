package com.ttn.elasticsearchAPI.interceptor;

import com.ttn.elasticsearchAPI.util.ConfigHelper;
import groovy.lang.Closure;
import groovy.util.ConfigObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.util.Map;

import static java.util.Collections.emptyMap;
import static org.springframework.util.CollectionUtils.isEmpty;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@ControllerAdvice

public class ResponseModifier implements ResponseBodyAdvice<Object> {

    private final ConfigHelper configHelper;

    @Autowired
    public ResponseModifier(ConfigHelper configHelper) {
        this.configHelper = configHelper;
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        Map value = getMap(request);
        if (!isEmpty(value)) {
            Map processors = (Map) value.get("processors");
            if (!isEmpty(processors)) {
                Map post = (Map) processors.get("post");
                if (!isEmpty(post)) {
                    ((Closure) post.get("json")).call(body, returnType, selectedContentType, selectedConverterType, request, response);
                }
            }
        }
        return body;
    }

    private Map getMap(ServerHttpRequest request) {
        final String uri = request.getURI().getPath();
        final RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod().name());
        final ConfigObject urlMapping = (ConfigObject) configHelper.getAPIConfig();
        Map value = emptyMap();
        if (!isEmpty(urlMapping)) {
            if (requestMethod == GET) {
                value = getMap(uri, (ConfigObject) urlMapping.get("get"));
            } else if (requestMethod == POST) {
                value = getMap(uri, (ConfigObject) urlMapping.get("post"));
            } else {
                value = emptyMap();
            }
        }
        return value;
    }

    private Map getMap(final String uri, final ConfigObject configObject) {
        Map value = emptyMap();
        if (!isEmpty(configObject)) {
            for (Object key : configObject.keySet()) {
                value = (Map) configObject.get(key);
                if (!isEmpty(value)) {
                    if (!((String) value.get("uri")).contentEquals(uri)) {
                        value = emptyMap();
                    }
                }
            }
        }
        return value;
    }
}

