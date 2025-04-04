package com.dalmuina.pruebamango.heroes.domain

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dalmuina.pruebamango.core.domain.NetworkError
import com.dalmuina.pruebamango.core.domain.Result
import com.dalmuina.pruebamango.heroes.data.HeroRepository
import com.dalmuina.pruebamango.heroes.domain.model.Hero
import kotlinx.serialization.SerializationException
import java.io.IOException

class HeroesDataSource(
    private val repository: HeroRepository,
    private val filter: String = ""
) : PagingSource<Int, Hero>() {

    override fun getRefreshKey(state: PagingState<Int, Hero>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Hero> {
        val page = params.key ?: 1

        return when (val result = repository.getAllHeroesPagingFromApi(page, params.loadSize)) {
            is Result.Success -> {
                val filteredResults = result.data.results.filter {
                    it.name.contains(filter, ignoreCase = true)
                }

                LoadResult.Page(
                    data = filteredResults,
                    prevKey = if (page == 1) null else page - 1,  // Correct previous key
                    nextKey = if (filteredResults.isNotEmpty()) page + 1 else null // Ensure proper next key
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
