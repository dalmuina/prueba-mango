package com.dalmuina.pruebamango.heroes.data.network.dto

import com.google.gson.annotations.SerializedName

data class HeroDto(
    @SerializedName("id")val id:Int,
    @SerializedName("name")val name:String,
    @SerializedName("background_image")val backgroundImage: String
)
