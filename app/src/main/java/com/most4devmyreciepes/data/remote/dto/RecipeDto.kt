package com.most4devmyreciepes.data.remote.dto

import com.google.gson.annotations.SerializedName

data class RecipeDto(
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val title: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("thumbnail_url")
    val imageUrl: String,
    @SerializedName("sections")
    val sections: List<SectionsDto>,
    @SerializedName("instructions")
    val instructions: List<InstructionDto>,
    @SerializedName("cook_time_minutes")
    val cookingTime: Int,
    @SerializedName("num_servings")
    val servings: Int,
    @SerializedName("nutrition")
    val nutrition: NutritionDto,
) 