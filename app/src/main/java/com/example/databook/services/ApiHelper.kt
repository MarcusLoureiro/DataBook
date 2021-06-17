package com.example.databook.services

class ApiHelper(private val service: Service) {
    private val keyApi = "AIzaSyCGBvxxhR2IOAXrXkPsM8S8qu--_CCz3H0"

    suspend fun getSearch(string: String) = service.getSearch(string, keyApi)
}