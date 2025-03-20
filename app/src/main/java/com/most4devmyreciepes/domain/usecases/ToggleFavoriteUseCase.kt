package com.most4devmyreciepes.domain.usecases

import com.most4devmyreciepes.domain.entity.Recipe
import com.most4devmyreciepes.domain.repository.RecipeRepository
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(
    private val repository: RecipeRepository
) {
    suspend operator fun invoke(recipe: Recipe) {
        repository.toggleFavorite(recipe)
    }
} 