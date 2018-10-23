package com.poc.runtimeEndpoints;

/*
Created by vishnu on 11/10/18 2:41 PM
*/

import groovy.util.ConfigObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestController
@RestControllerAdvice
public class GenericController {

    private final ConfigObject configObject;

    @Autowired
    public GenericController(final ConfigObject configObject) {
        this.configObject = configObject;
    }

    public Object getRequest() {
        return "";
    }

    public Object postRequest(@RequestBody String json) {
        return json;
    }

}