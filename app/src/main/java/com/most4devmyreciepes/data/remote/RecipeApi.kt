package com.most4devmyreciepes.data.remote

import com.most4devmyreciepes.data.remote.dto.RecipeDto
import com.most4devmyreciepes.data.remote.dto.ResponseRecipesDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipeApi {
    @GET("recipes/list")
    suspend fun getRecipes(@Query("from") from: Int = 0, @Query("size") size: Int = 20): ResponseRecipesDto

    @GET("recipes/get-more-info")
    suspend fun getRecipeById(@Query("id") id: Long): RecipeDto

    companion object {
        const val BASE_URL = "https://tasty.p.rapidapi.com/"
    }
} 