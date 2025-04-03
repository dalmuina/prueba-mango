package com.dalmuina.pruebamango.heroes.data

import com.dalmuina.pruebamango.heroes.data.Constants.API_KEY
import com.dalmuina.pruebamango.heroes.data.Constants.ENDPOINT
import com.dalmuina.pruebamango.heroes.data.network.dto.HeroDetailDto
import com.dalmuina.pruebamango.heroes.data.network.dto.HeroesResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiClient {

    @GET(ENDPOINT + API_KEY)
    suspend fun getAllHeroesPagingFromApi(
        @Query("page") page:Int,
        @Query("page_size") pageSize:Int ): Response<HeroesResponseDto>

    @GET("$ENDPOINT/{id}$API_KEY")
    suspend fun getHeroById(@Path("id") id: Int): Response<HeroDetailDto>
}
