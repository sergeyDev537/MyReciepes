package com.most4devmyreciepes.ui.screens.home

import com.most4devmyreciepes.domain.entity.Recipe
import kotlinx.coroutines.flow.StateFlow

interface HomeComponent {

    val model: StateFlow<HomeStore.State>

    fun onFavoriteClick(recipe: Recipe)

    fun onRecipeClick(recipeId: Long)
}