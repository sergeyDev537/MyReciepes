package com.most4devmyreciepes.data.remote.dto

import com.google.gson.annotations.SerializedName

data class MeasurementDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("quantity")
    val quantity: String,
    @SerializedName("unit")
    val unit: UnitMeasurementDto
)