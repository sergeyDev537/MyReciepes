package com.most4devmyreciepes.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UnitMeasurementDto(
    @SerializedName("abbreviation")
    val abbreviation: String,
    @SerializedName("display_plural")
    val namePlural: String,
    @SerializedName("display_singular")
    val nameSingular: String,
    @SerializedName("name")
    val nameDefault: String,
    @SerializedName("system")
    val system: String
)