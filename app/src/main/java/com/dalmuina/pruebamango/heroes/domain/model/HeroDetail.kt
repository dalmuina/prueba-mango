package com.dalmuina.pruebamango.heroes.domain.model

import com.dalmuina.pruebamango.heroes.data.network.dto.HeroDetailDto

data class HeroDetail(
    val name : String,
    val descriptionRaw : String,
    val metacritic: Int?,
    val website : String,
    val backgroundImage: String
)

fun HeroDetailDto.toHeroDetail(): HeroDetail{
    return HeroDetail(
        name = name,
        descriptionRaw = descriptionRaw,
        metacritic = metacritic,
        website = website,
        backgroundImage = backgroundImage
    )
}
