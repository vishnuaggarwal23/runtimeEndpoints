package com.ttn.elasticsearchAPI.service;

import com.ttn.elasticsearchAPI.dto.ResponseDTO;
import com.ttn.elasticsearchAPI.dto.SearchDTO;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.io.IOException;


@Service
public class GenericService {

    private RestClient restClient;

    private GenericService() {
        restClient = RestClient.builder(
                new HttpHost("localhost", 9200),
                new HttpHost("localhost", 9201)
        ).build();
    }

    public ResponseDTO search(SearchDTO dto) throws IOException {
        HttpEntity entity = new NStringEntity(dto.getQuery(), ContentType.APPLICATION_JSON);
        Response response = restClient.performRequest(
                dto.getRequestMethod(),
                dto.getPath(),
                dto.getResponseFilters(),
                entity
        );
        return new ResponseDTO(response, dto);
    }

    @PreDestroy
    void shutdown() throws IOException {
        restClient.close();
    }

}
