package com.dalmuina.pruebamango.heroes.domain.model

import com.dalmuina.pruebamango.heroes.data.network.dto.HeroesResponseDto

data class HeroesResponse(
    val count: Int,
    val results: List<Hero>
)

fun HeroesResponseDto.toHeroesResponse(): HeroesResponse{
    return HeroesResponse(
        count = count,
        results = results.map{hero->
            hero.toHero()
        }
    )
}
