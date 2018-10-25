package com.ttn.elasticsearchAPI.co;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SearchCO {

    @NotBlank
    String query;
    Integer offset = 0;
    Integer limit = 10;

}




