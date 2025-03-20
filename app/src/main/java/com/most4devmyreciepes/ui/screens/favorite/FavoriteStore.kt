package com.most4devmyreciepes.ui.screens.favorite

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.most4devmyreciepes.domain.entity.Recipe
import com.most4devmyreciepes.domain.usecases.GetFavoriteRecipesUseCase
import com.most4devmyreciepes.domain.usecases.ToggleFavoriteUseCase
import com.most4devmyreciepes.ui.screens.favorite.FavoriteStore.Intent
import com.most4devmyreciepes.ui.screens.favorite.FavoriteStore.Label
import com.most4devmyreciepes.ui.screens.favorite.FavoriteStore.State
import kotlinx.coroutines.launch
import javax.inject.Inject

interface FavoriteStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data class ToggleFavorite(val recipe: Recipe) : Intent
        data class NavigateToRecipe(val recipeId: Long) : Intent
    }

    data class State(val state: FavRecipeState) {
        sealed interface FavRecipeState {
            data class Loaded(val recipes: List<Recipe>) : FavRecipeState
            data object LoadedEmpty : FavRecipeState
            data object Initial : FavRecipeState
        }
    }

    sealed interface Label {
        data class NavigateToRecipe(val recipeId: Long) : Label
    }
}

class FavoriteStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val getFavoriteRecipes: GetFavoriteRecipesUseCase,
    private val toggleFavorite: ToggleFavoriteUseCase,
) {

    fun create(): FavoriteStore =
        object : FavoriteStore, Store<Intent, State, Label> by storeFactory.create(
            name = "FavoriteStore",
            initialState = State(state = State.FavRecipeState.Initial),
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
        data class LoadRecipes(val recipes: List<Recipe>) : Action
    }

    private sealed interface Msg {
        data class RecipesLoaded(val recipes: List<Recipe>) : Msg
    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                getFavoriteRecipes.invoke().collect { recipes ->
                    dispatch(Action.LoadRecipes(recipes))
                }
            }
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                is Intent.ToggleFavorite -> {
                    scope.launch {
                        toggleFavorite.invoke(intent.recipe)
                    }
                }

                is Intent.NavigateToRecipe -> {
                    publish(Label.NavigateToRecipe(intent.recipeId))
                }
            }
        }

        override fun executeAction(action: Action, getState: () -> State) {
            when (action) {
                is Action.LoadRecipes -> {
                    dispatch(Msg.RecipesLoaded(action.recipes))
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.RecipesLoaded -> {
                    val isEmpty = msg.recipes.isEmpty()
                    val newState = if (isEmpty) {
                        State.FavRecipeState.LoadedEmpty
                    } else {
                        State.FavRecipeState.Loaded(msg.recipes)
                    }
                    copy(state = newState)
                }
            }
    }
}