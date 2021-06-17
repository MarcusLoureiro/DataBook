package com.example.databook.services

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {
        private const val urlApiGoogleBooks = "https://www.googleapis.com/books/v1/"
        private fun getRetrofit(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(urlApiGoogleBooks)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        val service: Service = getRetrofit().create(Service::class.java)
    }