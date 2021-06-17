package com.example.databook.services


class MainRepository(private val apiHelper: ApiHelper) {

    suspend fun getSearch(string: String) = apiHelper.getSearch(string)
}
