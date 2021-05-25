package com.example.isbm.Services

import com.example.isbm.Entities.Books
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface Service {


    //
    @GET("volumes?")
    suspend fun getSearch(
            @Query("q") searchText: String,
            @Query("key") key: String?): Books

//    @GET("volumes?langRestrict=en&maxResults=40&printType=books")
//    suspend fun searchBooks(@Query("q") searchText: String?,
//                    @Query("key") key: String?): Books
}


const val urlApiGoogleBooks = "https://www.googleapis.com/books/v1/"
const val keyApi = "AIzaSyCGBvxxhR2IOAXrXkPsM8S8qu--_CCz3H0"

var gson = GsonBuilder()
    .setLenient()
    .create()


val retrofit = Retrofit.Builder()
    .baseUrl(urlApiGoogleBooks)
    .addConverterFactory(GsonConverterFactory.create(gson))
    .build()

val service: Service = retrofit.create(Service::class.java)