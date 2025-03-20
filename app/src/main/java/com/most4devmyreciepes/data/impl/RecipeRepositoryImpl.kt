package com.most4devmyreciepes.data.impl

import com.most4devmyreciepes.data.local.dao.RecipeDao
import com.most4devmyreciepes.data.mapper.toRecipe
import com.most4devmyreciepes.data.mapper.toRecipeDbModel
import com.most4devmyreciepes.data.mapper.toRecipeList
import com.most4devmyreciepes.data.remote.RecipeApi
import com.most4devmyreciepes.domain.entity.Recipe
import com.most4devmyreciepes.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RecipeRepositoryImpl @Inject constructor(
    private val api: RecipeApi,
    private val dao: RecipeDao,
) : RecipeRepository {

    override fun getRecipes(): Flow<List<Recipe>> =
        combine(
            dao.getFavoriteRecipes(),
            loadRecipes()
        ) { favsRecipes, responseRecipes ->
            val favsRecipesIds = favsRecipes.map { it.id }.toSet()
            val recipes = responseRecipes.results.toRecipeList(favsRecipesIds)

            recipes
        }

    private fun loadRecipes() = flow {
        emit(api.getRecipes())
    }

    override fun getRecipeById(id: Long): Flow<Recipe?> = combine(
        loadRecipe(id),
        dao.isRecipeFavorite(id)
    ) { recipe, isFavourite ->
        val updatedRecipe = recipe.copy(isFavorite = isFavourite)
        updatedRecipe
    }

    private fun loadRecipe(recipeId: Long) = flow {
        emit(api.getRecipeById(recipeId).toRecipe())
    }

    override fun getFavoriteRecipes(): Flow<List<Recipe>> =
        dao.getFavoriteRecipes().map { favsRecipes ->
            favsRecipes.toRecipeList()
        }

    override suspend fun toggleFavorite(recipe: Recipe) {
        if (recipe.isFavorite) {
            dao.deleteRecipeById(recipe.id)
        } else {
            val updatedRecipe = recipe.copy(isFavorite = true)
            dao.insertRecipe(updatedRecipe.toRecipeDbModel())
        }
    }

    override fun isRecipeFavorite(recipeId: Long): Flow<Boolean> {
        return dao.isRecipeFavorite(recipeId)
    }
} 