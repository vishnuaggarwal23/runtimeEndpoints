package com.poc.runtimeEndpoints

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.RequestMethod

/*
Created by vishnu on 23/10/18 3:07 PM
*/

@Component
class UrlMappingUtil {

    @Autowired
    ConfigObject configObject

    ConfigObject getUrlMapping() {
        return configObject.get("urlMapping") as ConfigObject
    }

    ConfigObject getPostUrlMapping() {
        return getUrlMapping()?.get("post") as ConfigObject
    }

    ConfigObject getGetUrlMapping() {
        return getUrlMapping?.get("get") as ConfigObject
    }

    Map getPostUrlMappings() {
        Map postMap = [:]
        final ConfigObject post = getPostUrlMapping()
        post?.keySet()?.each {
            final ConfigObject value = post.get(it) as ConfigObject
            postMap.put(value.get("uri"), value)
        }
        return postMap
    }

    Map getGetUrlMappings() {
        Map getMap = [:]
        final ConfigObject get = getGetUrlMapping()
        get?.keySet()?.each {
            final ConfigObject value = get.get(it) as ConfigObject
            getMap.put(value.get("uri"), value)
        }
        return getMap
    }

    ConfigObject getUrlConfig(String uri, RequestMethod requestMethod) {
        if(requestMethod == RequestMethod.POST) {
            return getPostUrlMappings()?.get(uri) as ConfigObject
        }
        if(requestMethod == RequestMethod.GET) {
            return getGetUrlMappings()?.get(uri) as ConfigObject
        }
    }
}
