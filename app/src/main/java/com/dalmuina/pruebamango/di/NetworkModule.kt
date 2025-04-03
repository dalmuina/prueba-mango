package com.dalmuina.pruebamango.di

import com.dalmuina.pruebamango.heroes.data.ApiClient
import com.dalmuina.pruebamango.heroes.data.Constants.BASE_URL
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val NetworkModule = module{
    single {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    single {
        get<Retrofit>().create(ApiClient::class.java)
    }
}
