package com.most4devmyreciepes.domain.usecases

import com.most4devmyreciepes.domain.repository.RecipeRepository
import javax.inject.Inject

class GetRecipeByIdUseCase @Inject constructor(
    private val repository: RecipeRepository,
) {
    operator fun invoke(id: Long) = repository.getRecipeById(id)
} 