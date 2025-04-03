package com.dalmuina.pruebamango.heroes.data

import com.dalmuina.pruebamango.core.domain.NetworkError
import com.dalmuina.pruebamango.core.domain.Result
import com.dalmuina.pruebamango.core.domain.map
import com.dalmuina.pruebamango.heroes.data.network.dto.HeroDetailDto
import com.dalmuina.pruebamango.heroes.data.network.dto.HeroesResponseDto
import com.dalmuina.pruebamango.heroes.data.network.retrofit.safeApiCall
import com.dalmuina.pruebamango.heroes.domain.model.HeroDetail
import com.dalmuina.pruebamango.heroes.domain.model.HeroesResponse
import com.dalmuina.pruebamango.heroes.domain.model.toHeroDetail
import com.dalmuina.pruebamango.heroes.domain.model.toHeroesResponse

class HeroRepository(private val apiClient: ApiClient) {
    suspend fun getAllHeroesPagingFromApi(
        page: Int,
        pageSize: Int
    ): Result<HeroesResponse, NetworkError> {
        return safeApiCall<HeroesResponseDto> {
            apiClient.getAllHeroesPagingFromApi( page, pageSize)
        }.map { response ->
            response.toHeroesResponse()
        }
    }

    suspend fun getHeroById(id: Int): Result<HeroDetail, NetworkError> {
        return safeApiCall<HeroDetailDto> {
            apiClient.getHeroById(id)
        }.map {response ->
            response.toHeroDetail()
        }
    }
}
