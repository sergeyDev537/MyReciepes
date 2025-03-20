package com.most4devmyreciepes.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.most4devmyreciepes.data.local.dbmodels.RecipeDbModel
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {
    @Query("SELECT * FROM recipes")
    fun getFavoriteRecipes(): Flow<List<RecipeDbModel>>

    @Query("SELECT EXISTS(SELECT 1 FROM recipes WHERE id = :recipeId)")
    fun isRecipeFavorite(recipeId: Long): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: RecipeDbModel)

    @Query("DELETE FROM recipes WHERE id = :recipeId")
    suspend fun deleteRecipeById(recipeId: Long)
} 