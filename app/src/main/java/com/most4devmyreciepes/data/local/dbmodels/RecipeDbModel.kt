package com.most4devmyreciepes.data.local.dbmodels

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class RecipeDbModel(
    @PrimaryKey
    val id: Long,
    val title: String,
    val description: String,
    val imageUrl: String,
    val ingredients: List<String>,
    val instructions: List<String>,
    val cookingTime: Int,
    val calories: Int,
    val servings: Int,
    val isFavorite: Boolean = true,
) 