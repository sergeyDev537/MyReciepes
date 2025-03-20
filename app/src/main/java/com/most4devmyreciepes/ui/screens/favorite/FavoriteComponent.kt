package com.most4devmyreciepes.ui.screens.favorite

import com.most4devmyreciepes.domain.entity.Recipe
import kotlinx.coroutines.flow.StateFlow

interface FavoriteComponent {
    val model: StateFlow<FavoriteStore.State>

    fun onRecipeClick(recipeId: Long)

    fun onToggleFavorite(recipe: Recipe)
} 