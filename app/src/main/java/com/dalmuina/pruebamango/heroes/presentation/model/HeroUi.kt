package com.dalmuina.pruebamango.heroes.presentation.model

import com.dalmuina.pruebamango.heroes.domain.model.Hero

data class HeroUi(
    val id : Int,
    val name: String,
    val backgroundImage: String
)

fun Hero.toHeroUi() = HeroUi(id,name,backgroundImage)

internal val previewHeroUi = HeroUi(
    id = 1,
    name = "SpiderMan",
    backgroundImage = "https://example_image.jpg"
)
