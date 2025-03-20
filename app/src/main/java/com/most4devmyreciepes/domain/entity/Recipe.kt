package com.most4devmyreciepes.domain.entity

data class Recipe(
    val id: Long,
    val title: String,
    val description: String,
    val imageUrl: String,
    val ingredients: List<String>,
    val instructions: List<String>,
    val cookingTime: Int,
    val servings: Int,
    val calories: Int,
    val isFavorite: Boolean,
) 