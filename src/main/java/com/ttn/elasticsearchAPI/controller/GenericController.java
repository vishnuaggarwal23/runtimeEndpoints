package com.ttn.elasticsearchAPI.controller;

/*
Created by vishnu on 11/10/18 2:41 PM
*/

import com.ttn.elasticsearchAPI.co.SearchCO;
import com.ttn.elasticsearchAPI.dto.SearchDTO;
import com.ttn.elasticsearchAPI.service.GenericService;
import com.ttn.elasticsearchAPI.util.ConfigHelper;
import com.ttn.elasticsearchAPI.util.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@RestControllerAdvice
public class GenericController {

    private final GenericService genericService;

    private final QueryBuilder queryBuilder;

    private final ConfigHelper configHelper;

    @Autowired
    public GenericController(ConfigHelper configHelper, GenericService genericService, QueryBuilder queryBuilder) {
        this.configHelper = configHelper;
        this.genericService = genericService;
        this.queryBuilder = queryBuilder;
    }

    public Object getRequest() {
        return "";
    }

    public String postRequest(@Valid @RequestBody SearchCO searchCO, HttpServletRequest httpServletRequest) {
        String response = "";
        try {
            response = genericService.search(generateSearchDTO(searchCO, httpServletRequest));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return response;
    }

    private SearchDTO generateSearchDTO(SearchCO searchCO, HttpServletRequest currentRequest) throws JSONException {
        String query = queryBuilder.generateSearchQuery(searchCO);
        return new SearchDTO(query, configHelper.getSearchIndexPath(currentRequest), currentRequest.getMethod());
    }
}