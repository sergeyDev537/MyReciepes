package com.most4devmyreciepes.domain.usecases

import com.most4devmyreciepes.domain.entity.Recipe
import com.most4devmyreciepes.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetRecipesUseCase @Inject constructor(
    private val repository: RecipeRepository,
) {
    operator fun invoke(): Flow<List<Recipe>> = repository.getRecipes()
} 