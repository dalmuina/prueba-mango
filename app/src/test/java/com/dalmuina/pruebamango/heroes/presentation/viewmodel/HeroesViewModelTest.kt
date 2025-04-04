package com.dalmuina.pruebamango.heroes.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingData
import app.cash.turbine.test
import com.dalmuina.pruebamango.heroes.data.HeroRepository
import com.dalmuina.pruebamango.heroes.domain.usecase.GetHeroByIdUseCase
import com.google.common.truth.Truth.assertThat
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.junit4.MockKRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import com.dalmuina.pruebamango.core.domain.NetworkError
import com.dalmuina.pruebamango.core.domain.Result
import com.dalmuina.pruebamango.heroes.domain.model.Hero
import com.dalmuina.pruebamango.heroes.domain.model.HeroDetail
import com.dalmuina.pruebamango.heroes.domain.model.HeroesResponse
import com.dalmuina.pruebamango.heroes.presentation.HeroListAction
import com.dalmuina.pruebamango.heroes.presentation.model.HeroDetailUi
import com.dalmuina.pruebamango.heroes.presentation.model.toHeroDetailUi
import io.mockk.coEvery
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle


@OptIn(ExperimentalCoroutinesApi::class)
class HeroesViewModelTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @RelaxedMockK
    private lateinit var repository: HeroRepository

    @RelaxedMockK
    private lateinit var getHeroByIdUseCase: GetHeroByIdUseCase

    private lateinit var viewModel: HeroesViewModel



    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModel = HeroesViewModel(
            repository,
            getHeroByIdUseCase,
            testDispatcher
        )
    }

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @Test
    fun `getHeroesPagingFlow initial state`() = testScope.runTest{
        // Given
        val job = launch { viewModel.heroesPagingFlow.collect() }
        // Then
        assertThat(viewModel.filter.value).isEqualTo("")
        job.cancel()
    }


    @Test
    fun `getHeroesPagingFlow with empty filter`() = testScope.runTest {
        // Given
        val heroesList = listOf(
            Hero(id = 1, name = "SpiderMan", backgroundImage = "image"),
            Hero(id = 2, name = "IronMan", backgroundImage = "image")
        )
        val pagingData = PagingData.from(heroesList)

        coEvery { repository.getAllHeroesPagingFromApi(any(), any()) } returns Result.Success(
            HeroesResponse(1,heroesList)
        )

        val job = launch {
            viewModel.heroesPagingFlow.test {
                val result = awaitItem()
                assertThat(result).isEqualTo(heroesList)
            }
        }

        advanceUntilIdle()
        job.cancel()
    }



    @Test
    fun `getHeroesPagingFlow error from HeroesDataSource`() = testScope.runTest {
        // Given
        coEvery { repository.getAllHeroesPagingFromApi(any(), any()) } returns Result.Error(NetworkError.NO_INTERNET)

        val job = launch {
            viewModel.heroesPagingFlow.test {
                val result = awaitItem()
                assertThat(result).isEqualTo(emptyList<Hero>())
            }
        }

        advanceUntilIdle()
        job.cancel()
    }


    @Test
    fun `getDetail initial state`() = testScope.runTest {
        //Given
        val mockHeroDetail = HeroDetailUi(name = "", descriptionRaw = "",
            metacritic = 0, website = "", backgroundImage = "")
        // When
        val initialState = viewModel.detail.value

        // Then
        assertThat(initialState.isLoading).isFalse()
        assertThat(initialState.heroDetailUi).isEqualTo(mockHeroDetail)
    }

    @Test
    fun `loadGameDetail success`() = testScope.runTest {
        // Given
        val heroId = 1
        val mockHero = HeroDetail(name = "Spiderman", descriptionRaw = "Hero",
            metacritic = 30, website = "web", backgroundImage = "image")
        val expectedHeroDetail = mockHero.toHeroDetailUi()

        coEvery { getHeroByIdUseCase(heroId) } returns Result.Success(mockHero)

        // When
        viewModel.loadGameDetail(heroId)
        advanceUntilIdle()

        // Then
        val state = viewModel.detail.value
        assertThat(state.isLoading).isFalse()
        assertThat(state.heroDetailUi).isEqualTo(expectedHeroDetail)
    }

    @Test
    fun `loadGameDetail failure`() = testScope.runTest {
        // Given
        val heroId = 1
        val mockHeroDetail = HeroDetailUi(name = "", descriptionRaw = "",
            metacritic = 0, website = "", backgroundImage = "")

        coEvery { getHeroByIdUseCase(heroId) } returns Result.Error(NetworkError.UNKNOWN)

        // When
        viewModel.loadGameDetail(heroId)
        advanceUntilIdle()

        // Then
        val state = viewModel.detail.value
        assertThat(state.isLoading).isFalse()
        assertThat(state.heroDetailUi).isEqualTo(mockHeroDetail)
        // You might want to verify error handling here if you have any
    }

    @Test
    fun `onAction OnBackButtonClick`() {
        // Given no state change should happen

        // When
        viewModel.onAction(HeroListAction.OnBackButtonClick)

        // Then
        assertThat(viewModel.filter.value).isEqualTo(viewModel.filter.value)
    }


}