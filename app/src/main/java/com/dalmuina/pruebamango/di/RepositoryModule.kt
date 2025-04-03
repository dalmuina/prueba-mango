package com.dalmuina.pruebamango.di

import com.dalmuina.pruebamango.heroes.data.HeroRepository
import org.koin.dsl.module

val RepositoryModule = module {
    single {
        HeroRepository(apiClient = get())
    }
}

