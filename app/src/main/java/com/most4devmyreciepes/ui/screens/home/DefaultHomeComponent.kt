package com.most4devmyreciepes.ui.screens.home

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


class DefaultHomeComponent @AssistedInject constructor(
    private val storeFactory: HomeStoreFactory,
    @Assisted("componentContext") private val componentContext: ComponentContext,
    @Assisted("onClickToRecipe") private val onClickToRecipe: (Long) -> Unit,
) : HomeComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { storeFactory.create() }
    private val scope = componentScope()

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<HomeStore.State> = store.stateFlow

    init {
        scope.launch {
            store.labels.collect {
                when (it) {
                    is HomeStore.Label.RecipeClicked -> {
                        onClickToRecipe.invoke(it.recipeId)
                    }
                }
            }
        }
    }

    override fun onFavoriteClick(recipe: Recipe) {
        store.accept(HomeStore.Intent.ToggleFavorite(recipe))
    }

    override fun onRecipeClick(recipeId: Long) {
        store.accept(HomeStore.Intent.RecipeClick(recipeId))
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("componentContext") componentContext: ComponentContext,
            @Assisted("onClickToRecipe") onClickToRecipe: (Long) -> Unit,
        ): DefaultHomeComponent
    }

}