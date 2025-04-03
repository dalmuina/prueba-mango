package com.dalmuina.pruebamango.heroes.domain.model

import com.dalmuina.pruebamango.heroes.data.network.dto.HeroDto

data class Hero(
    val id:Int,
    val name:String,
    val backgroundImage: String
)

fun HeroDto.toHero(): Hero{
    return Hero(
        id = id,
        name = name,
        backgroundImage = backgroundImage
    )
}
