package com.most4devmyreciepes.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.most4devmyreciepes.ui.components.RecipeCard

@Composable
fun HomeContent(
    component: HomeComponent,
) {
    val state = component.model.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        when (val currentState = state.value) {
            is HomeStore.State.Initial -> {
                // Initial state is handled by ViewModel
            }

            is HomeStore.State.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            is HomeStore.State.Loaded -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(currentState.recipes) { recipe ->
                        RecipeCard(
                            imageUrl = recipe.imageUrl,
                            title = recipe.title,
                            isFavorite = recipe.isFavorite,
                            calories = 100,
                            cookingTime = recipe.cookingTime,
                            ingredients = recipe.ingredients.joinToString(","),
                            onFavoriteClick = { component.onFavoriteClick(recipe) },
                            onClick = { component.onRecipeClick(recipe.id) }
                        )
                    }
                }
            }

            is HomeStore.State.Error -> {
                Text(
                    text = currentState.message,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
} 