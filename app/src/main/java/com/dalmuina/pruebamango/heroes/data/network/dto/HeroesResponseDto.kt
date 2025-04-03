package com.dalmuina.pruebamango.heroes.data.network.dto

import com.google.gson.annotations.SerializedName

data class HeroesResponseDto(
    @SerializedName("count")val count: Int,
    @SerializedName("results")val results: List<HeroDto>
)
