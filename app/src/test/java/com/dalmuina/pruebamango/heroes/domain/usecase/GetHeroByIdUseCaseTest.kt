package com.dalmuina.pruebamango.heroes.domain.usecase

import com.dalmuina.pruebamango.core.domain.NetworkError
import com.dalmuina.pruebamango.core.domain.Result
import com.dalmuina.pruebamango.heroes.data.HeroRepository
import com.dalmuina.pruebamango.heroes.domain.model.HeroDetail
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetHeroByIdUseCaseTest {

    @RelaxedMockK
    private lateinit var repository:  HeroRepository

    private lateinit var getHeroByIdUseCase: GetHeroByIdUseCase

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        getHeroByIdUseCase = GetHeroByIdUseCase(repository)
    }

    @Test
    fun `Successful retrieval of game details`() = runBlocking{
        //Given
        val mockHeroDetail = HeroDetail(
            name = "Gta V",
            descriptionRaw = "Description",
            metacritic = 67,
            website = "website url",
            backgroundImage = "background image"
        )
        val mockResult : Result<HeroDetail, Nothing> = Result.Success(mockHeroDetail)
        coEvery { repository.getHeroById(23) } returns mockResult
        //When
        val result = getHeroByIdUseCase(23)
        //Then
        assert(result == mockResult)
    }

    @Test
    fun `Repository returns a network error`() = runBlocking{
        //Given
        val mockResult : Result<Nothing, NetworkError> = Result.Error(NetworkError.NO_INTERNET)
        coEvery { repository.getHeroById(23) } returns mockResult
        //When
        val result = getHeroByIdUseCase(23)
        //Then
        assert(result == mockResult)
    }

    @Test
    fun `Repository returns a not found error`() = runBlocking{
        //Given
        val mockResult : Result<Nothing, NetworkError> = Result.Error(NetworkError.UNKNOWN)
        coEvery { repository.getHeroById(23) } returns mockResult
        //When
        val result = getHeroByIdUseCase(23)
        //Then
        assert(result == mockResult)
    }

    @Test
    fun `Repository returns a server error`() = runBlocking{
        //Given
        val mockResult : Result<Nothing, NetworkError> = Result.Error(NetworkError.SERVER_ERROR)
        coEvery { repository.getHeroById(23) } returns mockResult
        //When
        val result = getHeroByIdUseCase(23)
        //Then
        assert(result == mockResult)
    }

    @Test
    fun `ID is zero`() = runBlocking{
        //Given
        val mockHeroDetail = HeroDetail(
            name = "Gta V",
            descriptionRaw = "Description",
            metacritic = 67,
            website = "website url",
            backgroundImage = "background image"
        )
        val mockResult : Result<HeroDetail, Nothing> = Result.Success(mockHeroDetail)
        coEvery { repository.getHeroById(0) } returns mockResult
        //When
        val result = getHeroByIdUseCase(0)
        //Then
        assert(result == mockResult)
    }

    @Test
    fun `ID is negative`() = runBlocking{
        //Given
        val mockHeroDetail = HeroDetail(
            name = "Gta V",
            descriptionRaw = "Description",
            metacritic = 67,
            website = "website url",
            backgroundImage = "background image"
        )
        val mockResult : Result<HeroDetail, Nothing> = Result.Success(mockHeroDetail)
        coEvery { repository.getHeroById(-1) } returns mockResult
        //When
        val result = getHeroByIdUseCase(-1)
        //Then
        assert(result == mockResult)
    }

    @Test
    fun `ID is maximum integer value`() = runBlocking{
        //Given
        val mockHeroDetail = HeroDetail(
            name = "Gta V",
            descriptionRaw = "Description",
            metacritic = 67,
            website = "website url",
            backgroundImage = "background image"
        )
        val mockResult : Result<HeroDetail, Nothing> = Result.Success(mockHeroDetail)
        coEvery { repository.getHeroById(Int.MAX_VALUE) } returns mockResult
        //When
        val result = getHeroByIdUseCase(Int.MAX_VALUE)
        //Then
        assert(result == mockResult)
    }

    @Test
    fun `ID is minimum integer value`() = runBlocking{
        //Given
        val mockHeroDetail = HeroDetail(
            name = "Gta V",
            descriptionRaw = "Description",
            metacritic = 67,
            website = "website url",
            backgroundImage = "background image"
        )
        val mockResult : Result<HeroDetail, Nothing> = Result.Success(mockHeroDetail)
        coEvery { repository.getHeroById(Int.MIN_VALUE) } returns mockResult
        //When
        val result = getHeroByIdUseCase(Int.MIN_VALUE)
        //Then
        assert(result == mockResult)
    }
}
