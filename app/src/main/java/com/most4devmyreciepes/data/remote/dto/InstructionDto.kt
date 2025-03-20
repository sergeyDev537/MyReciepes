package com.most4devmyreciepes.data.remote.dto

import com.google.gson.annotations.SerializedName

data class InstructionDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("display_text")
    val description: String,
    @SerializedName("start_time")
    val startTime: Int,
    @SerializedName("end_time")
    val endTime: Int,
    @SerializedName("position")
    val position: Int,
)