package com.most4devmyreciepes.ui.screens.favorite

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.most4devmyreciepes.domain.entity.Recipe
import com.most4devmyreciepes.utils.componentScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DefaultFavoriteComponent @AssistedInject constructor(
    private val storeFactory: FavoriteStoreFactory,
    @Assisted("componentContext") private val componentContext: ComponentContext,
    @Assisted("onClickToRecipe") private val onClickToRecipe: (Long) -> Unit,
) : FavoriteComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { storeFactory.create() }
    private val scope = componentScope()

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<FavoriteStore.State> = store.stateFlow

    init {
        scope.launch {
            store.labels.collect {
                when (it) {
                    is FavoriteStore.Label.NavigateToRecipe -> {
                        onClickToRecipe.invoke(it.recipeId)
                    }
                }
            }
        }
    }

    override fun onRecipeClick(recipeId: Long) {
        store.accept(FavoriteStore.Intent.NavigateToRecipe(recipeId))
    }

    override fun onToggleFavorite(recipe: Recipe) {
        store.accept(FavoriteStore.Intent.ToggleFavorite(recipe))
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("componentContext") componentContext: ComponentContext,
            @Assisted("onClickToRecipe") onClickToRecipe: (Long) -> Unit,
        ): DefaultFavoriteComponent
    }

} 