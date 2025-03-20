package com.most4devmyreciepes.domain.usecases

import com.most4devmyreciepes.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class IsRecipeFavoriteUseCase @Inject constructor(
    private val repository: RecipeRepository
) {
    operator fun invoke(recipeId: Long): Flow<Boolean> = repository.isRecipeFavorite(recipeId)
} 