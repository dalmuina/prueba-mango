package com.dalmuina.pruebamango.di

import com.dalmuina.pruebamango.heroes.presentation.viewmodel.HeroesViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val ViewModelModule = module {
    viewModelOf(::HeroesViewModel)
}