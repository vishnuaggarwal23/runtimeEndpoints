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
                        query {
                            json = {

                            }
                        }
                        path = "/_mget"
                    }
                    processors {
                        pre {
                            json = { HttpServletRequest request, HttpServletResponse response, Object handler, Log log ->

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

