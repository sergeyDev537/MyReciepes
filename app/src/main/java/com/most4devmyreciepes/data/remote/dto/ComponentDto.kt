package com.most4devmyreciepes.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ComponentDto(
    @SerializedName("ingredient")
    val ingredient: IngredientDto,
    @SerializedName("measurements")
    val measurements: List<MeasurementDto>,
)