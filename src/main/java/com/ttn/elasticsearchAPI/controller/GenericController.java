package com.ttn.elasticsearchAPI.controller;

import com.ttn.elasticsearchAPI.co.SearchCO;
import com.ttn.elasticsearchAPI.dto.ResponseDTO;
import com.ttn.elasticsearchAPI.dto.SearchDTO;
import com.ttn.elasticsearchAPI.service.GenericService;
import com.ttn.elasticsearchAPI.util.ConfigHelper;
import com.ttn.elasticsearchAPI.util.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
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

    public ResponseDTO postRequest(@Valid @RequestBody SearchCO searchCO, HttpServletRequest httpServletRequest) {
        ResponseDTO responseDTO = null;
        try {
            responseDTO = genericService.search(generateSearchDTO(searchCO, httpServletRequest));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseDTO;
    }

    private SearchDTO generateSearchDTO(SearchCO searchCO, HttpServletRequest currentRequest) {
        return new SearchDTO(
                queryBuilder.generateSearchQuery(searchCO),
                configHelper.getSearchIndexPath(),
                currentRequest.getMethod(),
                configHelper.getResponseFilters(),
                searchCO.getLimit(),
                searchCO.getOffset()
        );
    }
}