package com.dalmuina.pruebamango.heroes.presentation

sealed interface HeroListAction {
    data class OnLoadHeroDetail(val id:Int): HeroListAction
    data object OnBackButtonClick: HeroListAction
}
