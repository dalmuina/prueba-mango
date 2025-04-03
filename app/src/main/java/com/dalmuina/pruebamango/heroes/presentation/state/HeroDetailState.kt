package com.dalmuina.pruebamango.heroes.presentation.state

import androidx.compose.runtime.Immutable
import com.dalmuina.pruebamango.heroes.presentation.model.HeroDetailUi

@Immutable
data class HeroDetailState(
    val isLoading: Boolean = false,
    val heroDetailUi: HeroDetailUi = HeroDetailUi(
        name = "",
        descriptionRaw = "",
        metacritic = 0,
        website = "",
        backgroundImage = ""
    )
)
