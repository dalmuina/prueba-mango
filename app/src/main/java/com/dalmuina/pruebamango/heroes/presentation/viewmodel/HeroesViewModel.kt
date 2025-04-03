package com.dalmuina.pruebamango.heroes.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import com.dalmuina.pruebamango.heroes.data.HeroRepository
import com.dalmuina.pruebamango.heroes.domain.HeroesDataSource
import com.dalmuina.pruebamango.heroes.presentation.HeroListAction
import com.dalmuina.pruebamango.heroes.presentation.model.toHeroUi
import com.dalmuina.pruebamango.heroes.presentation.navigation.Detail
import com.dalmuina.pruebamango.heroes.presentation.state.HeroDetailState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

class HeroesViewModel(
    private val repository: HeroRepository
): ViewModel() {

    val heroesPagingFlow = Pager(
        PagingConfig(
            pageSize = 20,
            enablePlaceholders = false
        )
    ) {
        HeroesDataSource(repository)
    }.flow
        .map{ pagingData->
            pagingData.map {
                it.toHeroUi()
            }
        }.cachedIn(viewModelScope)

    private val _detail = MutableStateFlow(HeroDetailState())
    val detail = _detail.asStateFlow()

    fun onAction(action: HeroListAction){
        when(action){
            is HeroListAction.OnLoadHeroDetail -> {
                loadGameDetail(action.id)
            }
            HeroListAction.OnBackButtonClick -> Unit
        }
    }

    fun loadGameDetail(id:Int){

    }
}
