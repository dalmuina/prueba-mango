package com.dalmuina.pruebamango.heroes.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import com.dalmuina.pruebamango.core.domain.onError
import com.dalmuina.pruebamango.core.domain.onSuccess
import com.dalmuina.pruebamango.heroes.data.HeroRepository
import com.dalmuina.pruebamango.heroes.domain.HeroesDataSource
import com.dalmuina.pruebamango.heroes.domain.usecase.GetHeroByIdUseCase
import com.dalmuina.pruebamango.heroes.presentation.HeroListAction
import com.dalmuina.pruebamango.heroes.presentation.model.toHeroDetailUi
import com.dalmuina.pruebamango.heroes.presentation.model.toHeroUi
import com.dalmuina.pruebamango.heroes.presentation.state.HeroDetailState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class HeroesViewModel(
    private val repository: HeroRepository,
    private val getHeroByIdUseCase: GetHeroByIdUseCase
): ViewModel() {

    private val _filter = MutableStateFlow("")
    val filter = _filter.asStateFlow()

    private val debouncedFilter = _filter
        .debounce(300)
        .distinctUntilChanged()

    val heroesPagingFlow = debouncedFilter
        .flatMapLatest { filterValue ->
            Pager(
                PagingConfig(
                    pageSize = 20,
                    enablePlaceholders = false
                )
            ) {
                HeroesDataSource(repository, filterValue)
            }.flow
        }.map { pagingData ->
            pagingData.map { it.toHeroUi() }
        }.cachedIn(viewModelScope)


    private val _detail = MutableStateFlow(HeroDetailState())
    val detail = _detail.asStateFlow()

    fun onAction(action: HeroListAction){
        when(action){
            is HeroListAction.OnLoadHeroDetail -> {
                loadGameDetail(action.id)
            }
            is HeroListAction.OnFilterChange -> {
                _filter.update {
                    action.filter }
            }
            HeroListAction.OnBackButtonClick -> Unit
        }
    }

    fun loadGameDetail(id:Int){
        viewModelScope.launch(Dispatchers.IO) {
            _detail.update { it.copy(
                isLoading = true
            )}
            getHeroByIdUseCase(id)
                .onSuccess { hero->
                    _detail.update {it.copy(
                        isLoading = false,
                        heroDetailUi = hero.toHeroDetailUi()
                    )}
                }.onError {error ->
                    _detail.update { it.copy(
                        isLoading = false
                    )}
                    //_events.send(NetworkErrorEvent.Error(error))
                }

        }
    }
}
