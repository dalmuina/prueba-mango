package com.dalmuina.pruebamango.di

import com.dalmuina.pruebamango.heroes.domain.usecase.GetHeroByIdUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val UseCaseModule = module{
    singleOf(::GetHeroByIdUseCase)
}
