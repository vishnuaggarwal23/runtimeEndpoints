urlMapping {
    post {[
                search {
                    url = "/search"
                    operation {
                        query {
                            json = """"""
                        }
                        path = "/_mget"
                    }
                    processors {
                        pre {
                            json = """"""
                        }
                        post {
                            json = """"""
                        }
                    }
                }]
    }
    get {[
                autocomplete {
                    url = "/autocomplete"
                    requestType = "GET"
                    operation {

                    }
                    processors {
                        pre {
                            json = """"""
                        }
                        post {
                            json = """"""
                        }
                    }
                }]
    }
}

