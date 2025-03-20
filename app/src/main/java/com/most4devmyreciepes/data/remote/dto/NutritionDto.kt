package com.most4devmyreciepes.data.remote.dto

import com.google.gson.annotations.SerializedName

data class NutritionDto(
    @SerializedName("calories")
    val calories: Int?,
    @SerializedName("carbohydrates")
    val carbohydrates: Int?,
    @SerializedName("fat")
    val fat: Int?,
    @SerializedName("fiber")
    val fiber: Int?,
    @SerializedName("protein")
    val protein: Int?,
    @SerializedName("sugar")
    val sugar: Int?,
)