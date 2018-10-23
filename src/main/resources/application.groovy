import org.apache.commons.logging.Log
import org.springframework.web.servlet.ModelAndView

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

urlMapping {
    post {
        [
                search {
                    uri = "/search"
                    operation {
                        query = """{
                                        "_source": [
                                            "name",
                                            "meeting.name",
                                            "meeting.location",
                                            "runner.name",
                                            "runner.trainer.name",
                                            "runner.jockey.name"
                                        ],
                                        "explain": true,
                                        "query": {
                                            "dis_max": {
                                                "queries": [
                                                    {
                                                        "multi_match": {
                                                            "query": "CUSTOM_SEARCH_QUERY",
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
                                                                    "query": "CUSTOM_SEARCH_QUERY",
                                                                    "fields": [
                                                                        "runner.name",
                                                                        "runner.trainer.name",
                                                                        "runner.jockey.name"
                                                                    ],
                                                                    "fuzziness": 1
                                                                }
                                                            },
                                                            "inner_hits": {}
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
                                        "size": 20,
                                        "from": 0
                                    }"""
                        path = "/_mget"
                    }
                    processors {
                        pre {
                            json = { HttpServletRequest request, HttpServletResponse response, Object handler, Log log ->
                                return true
                            }
                        }
                        post {
                            json = { HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView, Log log ->

                            }
                        }
                    }
                }]
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
                            json = { HttpServletRequest request, HttpServletResponse response, Object handler, Log log ->
                                return true
                            }
                        }
                        post {
                            json = { HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView, Log log ->

                            }
                        }
                    }
                }]
    }
}

