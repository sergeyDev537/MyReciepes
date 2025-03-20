package com.most4devmyreciepes.ui.screens.favorite

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.most4devmyreciepes.R
import com.most4devmyreciepes.ui.components.RecipeCard

@Composable
fun FavoriteContent(
    component: FavoriteComponent,
) {
    val model by component.model.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        when (val state = model.state) {
            is FavoriteStore.State.FavRecipeState.Initial -> {
                // Initial state is handled by the store
            }
            is FavoriteStore.State.FavRecipeState.Loaded -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 8.dp),
                ) {
                    items(state.recipes) { recipe ->
                        RecipeCard(
                            title = recipe.title,
                            imageUrl = recipe.imageUrl,
                            isFavorite = recipe.isFavorite,
                            cookingTime = recipe.cookingTime,
                            calories = recipe.calories,
                            ingredients = recipe.ingredients.joinToString(", "),
                            onClick = { component.onRecipeClick(recipe.id) },
                            onFavoriteClick = { component.onToggleFavorite(recipe) }
                        )
                    }
                }
            }
            is FavoriteStore.State.FavRecipeState.LoadedEmpty -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.empty_favorites_message),
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
} 