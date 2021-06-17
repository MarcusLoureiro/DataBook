package com.example.databook.services

import com.example.databook.entities.Books
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface Service {

    @GET("volumes?")
    suspend fun getSearch(
            @Query("q") searchText: String,
            @Query("key") key: String?): Books
}




