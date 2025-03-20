package com.most4devmyreciepes.ui.screens.details

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.most4devmyreciepes.R
import com.most4devmyreciepes.domain.entity.Recipe

@Composable
fun DetailsContent(component: DetailsComponent) {

    val state = component.model.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        when (val currentState = state.value) {
            is DetailsStore.State.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            is DetailsStore.State.Loaded -> {
                RecipeContent(
                    recipe = currentState.recipe,
                    onFavouriteClick = {
                        component.onToggleFavorite(currentState.recipe)
                    }
                )
            }

            is DetailsStore.State.Error -> {
                Text(
                    text = currentState.message,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            is DetailsStore.State.Initial -> {}
        }
    }

}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun RecipeContent(
    recipe: Recipe,
    onFavouriteClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 8.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        GlideImage(
            model = recipe.imageUrl,
            contentDescription = recipe.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.FillBounds
        )
        Spacer(modifier = Modifier.height(8.dp))
        MainInfoRecipe(
            name = recipe.title,
            isFavourite = recipe.isFavorite,
            cookingTimeMin = recipe.cookingTime,
            calories = recipe.calories,
            onFavouriteClick = onFavouriteClick,
        )
        Spacer(modifier = Modifier.height(8.dp))
        DescriptionRecipe(
            description = recipe.description,
            ingredients = recipe.ingredients,
        )
        Spacer(modifier = Modifier.height(8.dp))
        InstructionRecipe(
            instructions = recipe.instructions,
        )
    }
}

@Composable
private fun MainInfoRecipe(
    name: String,
    isFavourite: Boolean,
    cookingTimeMin: Int,
    calories: Int,
    onFavouriteClick: () -> Unit,
) {
    val iconFavs = if (isFavourite) Icons.Default.Favorite else Icons.Default.FavoriteBorder
    val contentDescFavs = stringResource(if (isFavourite) R.string.remove_from_favorites else R.string.add_to_favorites)

    Card(
        colors = CardDefaults.cardColors().copy(
            containerColor = MaterialTheme.colorScheme.surface,
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row {
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    text = name,
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                IconButton(onClick = onFavouriteClick) {
                    Icon(
                        imageVector = iconFavs,
                        contentDescription = contentDescFavs,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Row {
                Text(
                    modifier = Modifier
                        .weight(1f),
                    text = stringResource(R.string.minutes, cookingTimeMin),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Start,
                )
                Text(
                    modifier = Modifier
                        .weight(1f),
                    text = stringResource(R.string.calories, calories),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.End,
                )
            }
        }
    }
}

@Composable
private fun DescriptionRecipe(
    description: String,
    ingredients: List<String>,
) {
    Card(
        colors = CardDefaults.cardColors().copy(
            containerColor = MaterialTheme.colorScheme.surface,
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                modifier = Modifier
                    .padding(end = 8.dp),
                text = stringResource(R.string.description),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Start,
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                modifier = Modifier
                    .padding(end = 8.dp),
                text = stringResource(R.string.ingredients),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = ingredients.joinToString(", "),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Start,
            )
        }
    }
}

@Composable
private fun InstructionRecipe(instructions: List<String>) {
    Card(
        colors = CardDefaults.cardColors().copy(
            containerColor = MaterialTheme.colorScheme.surface,
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                text = stringResource(R.string.description),
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                buildAnnotatedString {
                    instructions.forEachIndexed { index, stepInstruction ->
                        withStyle(
                            SpanStyle(
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground,
                            )
                        ) {
                            append(stringResource(R.string.step_n, index + 1))
                            append("\n")
                        }
                        append(stepInstruction)
                        if (index != instructions.lastIndex) {
                            append("\n")
                            append("\n")
                        }
                    }
                }
            )
        }
    }
}