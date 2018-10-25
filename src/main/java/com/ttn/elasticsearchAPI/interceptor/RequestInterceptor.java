package com.ttn.elasticsearchAPI.interceptor;

/*
Created by vishnu on 15/10/18 1:16 PM
*/

import com.ttn.elasticsearchAPI.util.ConfigHelper;
import groovy.lang.Closure;
import groovy.util.ConfigObject;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

import static java.util.Collections.emptyMap;
import static org.springframework.util.CollectionUtils.isEmpty;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@CommonsLog
@Component
public class RequestInterceptor implements HandlerInterceptor {

    private final ConfigHelper configHelper;

    @Autowired
    public RequestInterceptor(ConfigHelper configHelper) {
        this.configHelper = configHelper;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean validPreHandling = false;
        Map value = getMap(request);
        if (!isEmpty(value)) {
            Map processors = (Map) value.get("processors");
            if (!isEmpty(processors)) {
                Map pre = (Map) processors.get("pre");
                if (!isEmpty(pre)) {
                    Object closure = ((Closure) pre.get("json")).call(request, response, handler);
                    if (closure instanceof Boolean) {
                        validPreHandling = (boolean) closure;
                    }
                }
            }
        }
        return validPreHandling;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        Map value = getMap(request);
        if (!isEmpty(value)) {
            Map processors = (Map) value.get("processors");
            if (!isEmpty(processors)) {
                Map post = (Map) processors.get("post");
                if (!isEmpty(post)) {
                    ((Closure) post.get("json")).call(request, response, handler, modelAndView);
                }
            }
        }
    }

    private Map getMap(HttpServletRequest request) {
        final String uri = request.getRequestURI();
        final RequestMethod requestMethod = RequestMethod.valueOf(request.getMethod());
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
