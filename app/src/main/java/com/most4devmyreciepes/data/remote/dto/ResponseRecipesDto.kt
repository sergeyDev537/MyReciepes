package com.most4devmyreciepes.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ResponseRecipesDto(
    @SerializedName("count")
    val count: Long,
    @SerializedName("results")
    val results: List<RecipeDto>,
)
