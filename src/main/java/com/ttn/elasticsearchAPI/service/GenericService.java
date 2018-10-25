package com.ttn.elasticsearchAPI.service;

import com.ttn.elasticsearchAPI.dto.SearchDTO;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.Collections;


@Service
public class GenericService {

    private RestClient restClient;

    private GenericService() {
        restClient = RestClient.builder(
                new HttpHost("localhost", 9200),
                new HttpHost("localhost", 9201)
        ).build();
    }

    public String search(SearchDTO dto) throws IOException {
        HttpEntity entity = new NStringEntity(dto.getQuery(), ContentType.APPLICATION_JSON);
        Response response = restClient.performRequest(
                dto.getRequestMethod(),
                dto.getPath(),
                Collections.singletonMap("pretty", "true"),
                entity
        );
        return EntityUtils.toString(response.getEntity());
    }

    @PreDestroy
    void shutdown() throws IOException {
        restClient.close();
    }

}
