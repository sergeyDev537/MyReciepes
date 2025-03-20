package com.most4devmyreciepes.domain.repository

import com.most4devmyreciepes.domain.entity.Recipe
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {
    fun getRecipes(): Flow<List<Recipe>>
    fun getRecipeById(id: Long): Flow<Recipe?>
    fun getFavoriteRecipes(): Flow<List<Recipe>>
    suspend fun toggleFavorite(recipe: Recipe)
    fun isRecipeFavorite(recipeId: Long): Flow<Boolean>
} 