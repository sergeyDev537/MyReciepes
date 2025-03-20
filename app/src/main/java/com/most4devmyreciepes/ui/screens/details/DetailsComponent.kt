package com.most4devmyreciepes.ui.screens.details

import com.most4devmyreciepes.domain.entity.Recipe
import kotlinx.coroutines.flow.StateFlow

interface DetailsComponent {

    val model: StateFlow<DetailsStore.State>

    fun onToggleFavorite(recipe: Recipe)

    fun clickBack()
}