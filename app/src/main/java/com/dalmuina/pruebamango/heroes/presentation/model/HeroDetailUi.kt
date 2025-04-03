package com.dalmuina.pruebamango.heroes.presentation.model

import com.dalmuina.pruebamango.heroes.domain.model.HeroDetail

data class HeroDetailUi(
    val name :String,
    val descriptionRaw: String,
    val metacritic: Int?,
    val website: String,
    val backgroundImage: String
)

fun HeroDetail.toHeroDetailUi(): HeroDetailUi{
    return HeroDetailUi(
        name = name,
        descriptionRaw = descriptionRaw,
        metacritic = metacritic,
        website = website,
        backgroundImage = backgroundImage
    )
}
