package com.sand.flickergalary

import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ServiceGenerator {

    companion object {
        private const val BASE_URL = "https://api.flickr.com/"

        private val retrofit: Retrofit by lazy { createRetrofit() }

        private fun createRetrofit(): Retrofit {
            var builder = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(Gson()))
                .baseUrl(BASE_URL)
            return builder.build()
        }

        fun <T> generateService(serviceClass: Class<T>): T {
            return retrofit.create(serviceClass)
        }
    }
}