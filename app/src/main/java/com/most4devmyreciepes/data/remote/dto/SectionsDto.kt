package com.most4devmyreciepes.data.remote.dto

import com.google.gson.annotations.SerializedName

data class SectionsDto(
    @SerializedName("components")
    val components: List<ComponentDto>,
)