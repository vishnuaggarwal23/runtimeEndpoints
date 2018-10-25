package com.ttn.elasticsearchAPI.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestMethod;


@Data
@AllArgsConstructor
public class SearchDTO {

    private String query;

    private String path;

    private String requestMethod;

}
