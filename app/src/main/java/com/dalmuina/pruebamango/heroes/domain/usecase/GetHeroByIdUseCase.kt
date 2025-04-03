package com.dalmuina.pruebamango.heroes.domain.usecase

import com.dalmuina.pruebamango.core.domain.NetworkError
import com.dalmuina.pruebamango.core.domain.Result
import com.dalmuina.pruebamango.heroes.data.HeroRepository
import com.dalmuina.pruebamango.heroes.domain.model.HeroDetail

class GetHeroByIdUseCase(private val repository: HeroRepository) {
    suspend operator fun invoke(id: Int): Result<HeroDetail, NetworkError> {
        return repository.getHeroById(id)
    }
}