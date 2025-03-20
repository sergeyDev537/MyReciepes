package com.most4devmyreciepes.data.mapper

import com.most4devmyreciepes.data.local.dbmodels.RecipeDbModel
import com.most4devmyreciepes.data.remote.dto.RecipeDto
import com.most4devmyreciepes.data.remote.dto.SectionsDto
import com.most4devmyreciepes.domain.entity.Recipe

fun RecipeDto.toRecipe(isFavorite: Boolean = false): Recipe {
    return Recipe(
        id = id,
        title = title,
        description = description,
        imageUrl = imageUrl,
        ingredients = sections.mapDtoIngredients(),
        instructions = instructions.map { it.description },
        cookingTime = if(cookingTime > 0) cookingTime else 30,
        servings = servings,
        calories = nutrition.calories ?: (100..300).random(),
        isFavorite = isFavorite
    )
}

fun List<SectionsDto>.mapDtoIngredients(): List<String> {
    val ingredients = mutableListOf<String>()
    this.forEach { section ->
        section.components.forEach { component ->
            ingredients.add(component.ingredient.nameDefault)
        }
    }
    return ingredients
}

fun List<RecipeDto>.toRecipeList(favoriteIds: Set<Long> = emptySet()): List<Recipe> {
    return map { dto ->
        dto.toRecipe(isFavorite = dto.id in favoriteIds)
    }
}

fun RecipeDbModel.toRecipe(): Recipe {
    return Recipe(
        id = id,
        title = title,
        description = description,
        imageUrl = imageUrl,
        ingredients = ingredients,
        instructions = instructions,
        cookingTime = cookingTime,
        servings = servings,
        calories = calories,
        isFavorite = isFavorite
    )
}

fun List<RecipeDbModel>.toRecipeList(): List<Recipe> {
    return map { it.toRecipe() }
}

fun Recipe.toRecipeDbModel(): RecipeDbModel {
    return RecipeDbModel(
        id = id,
        title = title,
        description = description,
        imageUrl = imageUrl,
        ingredients = ingredients,
        instructions = instructions,
        cookingTime = cookingTime,
        servings = servings,
        calories = calories,
        isFavorite = isFavorite
    )
}