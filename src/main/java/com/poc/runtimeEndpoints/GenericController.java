package com.poc.runtimeEndpoints;

/*
Created by vishnu on 11/10/18 2:41 PM
*/

import groovy.util.ConfigObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestController
@RestControllerAdvice
public class GenericController {

    private final UrlMappingUtil urlMappingUtil;

    @Autowired
    public GenericController(UrlMappingUtil urlMappingUtil) {
        this.urlMappingUtil = urlMappingUtil;
    }

    public Object getRequest() {
        return "";
    }

    public Object postRequest(@RequestBody String json, HttpServletRequest httpServletRequest) throws JSONException {
        final JSONObject jsonObject = new JSONObject(json);
        Object customSearchQuery = jsonObject.get("CUSTOM_SEARCH_QUERY");
        final ConfigObject value = urlMappingUtil.getUrlConfig(httpServletRequest.getRequestURI(), RequestMethod.POST);
        String query = (String) ((ConfigObject) value.get("operation")).get("query");
        query = StringUtils.replace(query, "CUSTOM_SEARCH_QUERY", customSearchQuery.toString());
        return query;
    }


}