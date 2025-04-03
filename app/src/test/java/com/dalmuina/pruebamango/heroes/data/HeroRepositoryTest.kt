package com.dalmuina.pruebamango.heroes.data

import com.dalmuina.pruebamango.core.domain.NetworkError
import com.dalmuina.pruebamango.core.domain.Result
import com.dalmuina.pruebamango.heroes.data.network.dto.HeroDto
import com.dalmuina.pruebamango.heroes.data.network.dto.HeroesResponseDto
import com.dalmuina.pruebamango.heroes.domain.model.HeroesResponse
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.IOException
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class HeroRepositoryTest {

    @RelaxedMockK
    private lateinit var apiClient: ApiClient

    private lateinit var heroRepository: HeroRepository

    @Before
    fun onBefore(){
        MockKAnnotations.init(this)
        heroRepository = HeroRepository(apiClient)
    }

    @Test
    fun `Successful API call with valid page and pageSize`() = runTest{
        //Given
        val mockHeroList = List(20) {
            HeroDto(id = it, name = "Name $it", backgroundImage = "Image $it")
        }
        val mockResponseDto = HeroesResponseDto(count = 20, results = mockHeroList)
        val mockResponse = Response.success(mockResponseDto)

        coEvery { apiClient.getAllHeroesPagingFromApi(1, 20) } returns mockResponse

        // When
        val result = heroRepository.getAllHeroesPagingFromApi(1, 20)

        // Then
        assertTrue(result is Result.Success)
        val successResult = result as Result.Success<HeroesResponse>
        assertEquals(20, successResult.data.results.size)
        assertEquals("Name 0", successResult.data.results[0].name)
    }

    @Test
    fun `API call returns a network error`() = runTest{
        //Given
        coEvery { apiClient.getAllHeroesPagingFromApi(1, 20) } throws IOException()

        // When
        val result = heroRepository.getAllHeroesPagingFromApi(1, 20)

        // Then
        assertTrue(result is Result.Error)
        assertEquals(NetworkError.UNKNOWN, (result as Result.Error).error)
    }

    @Test
    fun `API call returns an empty response`() = runTest{
        //Given
        val mockHeroList = emptyList<HeroDto>()
        val mockResponseDto = HeroesResponseDto(count = 20, results = mockHeroList)
        val mockResponse = Response.success(mockResponseDto)

        coEvery { apiClient.getAllHeroesPagingFromApi(1, 20) } returns mockResponse

        // When
        val result = heroRepository.getAllHeroesPagingFromApi(1, 20)

        // Then
        assertTrue(result is Result.Success)
        val successResult = result as Result.Success<HeroesResponse>
        assertEquals(20, successResult.data.count)
        assertTrue(successResult.data.results.isEmpty())
    }

    @Test
    fun `API call for first page`() = runTest{
        //Given
        val mockHeroList = List(20) {
            HeroDto(id = it, name = "Name $it", backgroundImage = "Image $it")
        }
        val mockResponseDto = HeroesResponseDto(count = 20, results = mockHeroList)
        val mockResponse = Response.success(mockResponseDto)

        coEvery { apiClient.getAllHeroesPagingFromApi(0, 20) } returns mockResponse

        // When
        val result = heroRepository.getAllHeroesPagingFromApi(0, 20)

        // Then
        assertTrue(result is Result.Success)
        val successResult = result as Result.Success<HeroesResponse>
        assertEquals(20, successResult.data.results.size)
        assertEquals("Name 0", successResult.data.results[0].name)
    }

    @Test
    fun `API call for the last page`() = runTest {
        // Given
        val mockHeroList = List(5) { // Smaller page indicating last page
            HeroDto(id = it, name = "Name $it", backgroundImage = "Image $it")
        }
        val mockResponseDto = HeroesResponseDto(count = 5, results = mockHeroList)
        val mockResponse = Response.success(mockResponseDto)

        coEvery { apiClient.getAllHeroesPagingFromApi(10, 20) } returns mockResponse

        // When
        val result = heroRepository.getAllHeroesPagingFromApi(10, 20)

        // Then
        assertTrue(result is Result.Success)
        assertEquals(5, (result as Result.Success).data.results.size)
    }

    @Test
    fun `API call with page size 1`() = runTest{
        // Given
        val singleHero = listOf(HeroDto(id = 1, name = "Single", backgroundImage = "Image"))
        val mockResponseDto = HeroesResponseDto(count = 1, results = singleHero)
        val mockResponse = Response.success(mockResponseDto)

        coEvery { apiClient.getAllHeroesPagingFromApi(1, 1) } returns mockResponse

        // When
        val result = heroRepository.getAllHeroesPagingFromApi(1, 1)

        // Then
        assertTrue(result is Result.Success)
        assertEquals(1, (result as Result.Success).data.results.size)
        assertEquals("Single", result.data.results[0].name)
    }

    @Test
    fun `API call with invalid page number  negative `() = runTest {
        //Given
        val errorResponse = Response.error<HeroesResponseDto>(400,
            "Bad Request".toResponseBody("text/plain".toMediaType()))

        coEvery { apiClient.getAllHeroesPagingFromApi(-1, 20) } returns errorResponse

        // When
        val result = heroRepository.getAllHeroesPagingFromApi(-1, 20)

        // Then
        assertTrue(result is Result.Error)
        assertEquals(NetworkError.UNKNOWN, (result as Result.Error).error)
    }

    @Test
    fun `API call with very large page number`() = runTest{
        // Given
        val errorResponse = Response.error<HeroesResponseDto>(400,
            "Page out of range".toResponseBody("text/plain".toMediaType()))

        coEvery { apiClient.getAllHeroesPagingFromApi(Int.MAX_VALUE, 20) } returns errorResponse

        // When
        val result = heroRepository.getAllHeroesPagingFromApi(Int.MAX_VALUE, 20)

        // Then
        assertTrue(result is Result.Error)
        assertEquals(NetworkError.UNKNOWN, (result as Result.Error).error)
    }

    @Test
    fun `API call with invalid page size (negative)`() = runTest {
        // Given
        val errorResponse = Response.error<HeroesResponseDto>(400,
            "Invalid page size".toResponseBody("text/plain".toMediaType()))

        coEvery { apiClient.getAllHeroesPagingFromApi(1, -20) } returns errorResponse

        // When
        val result = heroRepository.getAllHeroesPagingFromApi(1, -20)

        // Then
        assertTrue(result is Result.Error)
        assertEquals(NetworkError.UNKNOWN, (result as Result.Error).error)
    }

    @Test
    fun `Response mapping - HeroesResponseDto to HeroesResponse`() = runTest {
        // Given
        val mockHeroDto = HeroDto(id = 1, name = "Test Hero", backgroundImage = "test.jpg")
        val mockResponseDto = HeroesResponseDto(count = 1, results = listOf(mockHeroDto))
        val mockResponse = Response.success(mockResponseDto)

        coEvery { apiClient.getAllHeroesPagingFromApi(1, 1) } returns mockResponse

        // When
        val result = heroRepository.getAllHeroesPagingFromApi(1, 1)

        // Then
        assertTrue(result is Result.Success)
        val mappedHero = (result as Result.Success).data.results[0]
        assertEquals(mockHeroDto.id, mappedHero.id)
        assertEquals(mockHeroDto.name, mappedHero.name)
        assertEquals(mockHeroDto.backgroundImage, mappedHero.backgroundImage)
    }

    @Test
    fun `Server error returns correct NetworkError`() = runTest {
        // Given
        val errorResponse = Response.error<HeroesResponseDto>(500,
            "Server Error".toResponseBody("text/plain".toMediaType()))

        coEvery { apiClient.getAllHeroesPagingFromApi(1, 20) } returns errorResponse

        // When
        val result = heroRepository.getAllHeroesPagingFromApi(1, 20)

        // Then
        assertTrue(result is Result.Error)
        assertEquals(NetworkError.SERVER_ERROR, (result as Result.Error).error)
    }

}
