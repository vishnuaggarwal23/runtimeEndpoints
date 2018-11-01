package com.ttn.elasticsearchAPI.co;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Data
@ToString
public class SearchCO {

    @NotBlank
    String query;
    Integer offset = 0;
    Integer limit = 10;

}




