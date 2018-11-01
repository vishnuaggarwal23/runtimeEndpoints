import com.ttn.elasticsearchAPI.dto.ResponseDTO
import groovy.json.JsonSlurper
import org.springframework.core.MethodParameter
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.web.servlet.ModelAndView

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

elasticsearch {
    url = "http://localhost"
    port = 9200

}


api {
    post {

        searchAgain {
            uri = "/searchAgain"
            operation {
                path = "raceclips/_search"

            }
        }

        search {
            uri = "/search"
            operation {
                path = "raceclips/_search"
                query = """{
                                        "query": {
                                            "dis_max": {
                                                "queries": [
                                                    {
                                                        "multi_match": {
                                                            "query": "##SEARCH_QUERY##",
                                                            "fields": [
                                                                "name",
                                                                "meeting.name",
                                                                "meeting.location"
                                                            ],
                                                            "fuzziness": 1
                                                        }
                                                    },
                                                    {
                                                        "nested": {
                                                            "path": "runner",
                                                            "score_mode": "avg",
                                                            "query": {
                                                                "multi_match": {
                                                                    "query": "##SEARCH_QUERY##",
                                                                    "fields": [
                                                                        "runner.name",
                                                                        "runner.trainer.name",
                                                                        "runner.jockey.name"
                                                                    ],
                                                                    "fuzziness": 1
                                                                }
                                                            }
                                                        }
                                                    }
                                                ],
                                                "tie_breaker": 0.3
                                            }
                                        },
                                        "sort": [
                                            {
                                                "_score": {
                                                    "order": "desc"
                                                }
                                            },
                                            {
                                                "startTime": {
                                                    "order": "desc"
                                                }
                                            }
                                        ],
                                        "size": ##MAX##,
                                        "from": ##OFFSET##
                                    }"""
                responseFilters = "took,hits.total,hits.hits._source"
            }
            processors {
                pre {
                    json = { HttpServletRequest request, HttpServletResponse response, Object handlerRequestMethod ->
                        return true
                    }
                }
                post {
                    json = { ResponseDTO body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response ->
                        def responseJSON = new JsonSlurper().parseText(body.getSearchResponse())
                        Map modifiedResponse = [
                                data   : [
                                        totalCount: responseJSON.hits.total,
                                        max       : body.max,
                                        offset    : body.offset,
                                        items     : responseJSON.hits.hits
                                ],
                                message: body.status.statusCode == 200 ? "SUCCESS" : "FAILURE",
                                code   : responseJSON.hits.total as int ? 0 : 1
                        ]
                        modifiedResponse
                    }
                }
            }
        }
    }
    get {
        [
                autocomplete {
                    uri = "/autocomplete"
                    requestType = "GET"
                    operation {

                    }
                    processors {
                        pre {
                            json = { HttpServletRequest request, HttpServletResponse response, Object handlerRequestMethod ->
                                return true
                            }
                        }
                        post {
                            json = { HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndViewRequestMethod ->

                            }
                        }
                    }
                }
        ]
    }
}




