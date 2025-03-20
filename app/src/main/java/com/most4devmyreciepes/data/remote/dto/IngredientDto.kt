package com.most4devmyreciepes.data.remote.dto

import com.google.gson.annotations.SerializedName

data class IngredientDto(
    @SerializedName("display_plural")
    val namePlural: String,
    @SerializedName("display_singular")
    val nameSingular: String,
    @SerializedName("name")
    val nameDefault: String,
)