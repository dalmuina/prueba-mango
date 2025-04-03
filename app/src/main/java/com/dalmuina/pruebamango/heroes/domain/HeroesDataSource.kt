package com.dalmuina.pruebamango.heroes.domain

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dalmuina.pruebamango.core.domain.NetworkError
import com.dalmuina.pruebamango.core.domain.Result
import com.dalmuina.pruebamango.heroes.data.HeroRepository
import com.dalmuina.pruebamango.heroes.domain.model.Hero
import kotlinx.serialization.SerializationException
import java.io.IOException

class HeroesDataSource(private val repository: HeroRepository)
    : PagingSource<Int,Hero>()
{
    override fun getRefreshKey(state: PagingState<Int, Hero>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Hero> {
        return when (val result = repository.getAllHeroesPagingFromApi(
            page = params.key ?: 1,
            pageSize = params.loadSize
        )) {
            is Result.Success -> {
                LoadResult.Page(
                    data = result.data.results,
                    prevKey = null, // Only forward paging
                    nextKey = if (result.data.results.isNotEmpty()) {
                        (params.key ?: 1) + 1
                    } else null
                )
            }
            is Result.Error -> {
                LoadResult.Error(
                    when (result.error) {
                        NetworkError.NO_INTERNET -> IOException("No internet")
                        NetworkError.SERIALIZATION -> SerializationException()
                        else -> IOException("Network error")
                    }
                )
            }
        }
    }
}
