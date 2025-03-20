package com.most4devmyreciepes.ui.screens.home

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.most4devmyreciepes.domain.entity.Recipe
import com.most4devmyreciepes.domain.usecases.GetRecipesUseCase
import com.most4devmyreciepes.domain.usecases.ToggleFavoriteUseCase
import com.most4devmyreciepes.ui.screens.home.HomeStore.Intent
import com.most4devmyreciepes.ui.screens.home.HomeStore.Label
import com.most4devmyreciepes.ui.screens.home.HomeStore.State
import dagger.assisted.AssistedInject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.launch
import javax.inject.Inject

interface HomeStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data class ToggleFavorite(val recipe: Recipe) : Intent
        data class RecipeClick(val recipeId: Long) : Intent
    }

    sealed interface State {
        data object Initial : State
        data object Loading : State
        data class Loaded(val recipes: List<Recipe>) : State
        data class Error(val message: String) : State
    }

    sealed interface Label {
        data class RecipeClicked(val recipeId: Long) : Label
    }
}

class HomeStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val getRecipesUseCase: GetRecipesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
) {

    fun create(): HomeStore =
        object : HomeStore, Store<Intent, State, Label> by storeFactory.create(
            name = "HomeStore",
            initialState = State.Initial,
            bootstrapper = BootstrapperImpl(),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
        data object LoadingRecipes : Action
        data class LoadedRecipes(val recipes: List<Recipe>) : Action
        data class LoadingError(val message: String) : Action
        data class RecipeClick(val recipeId: Long) : Action
    }

    private sealed interface Msg {
        data object LoadingStarted : Msg
        data class RecipesLoaded(val recipes: List<Recipe>) : Msg
        data class Error(val message: String) : Msg
        data class FavoriteToggled(val recipe: Recipe) : Msg
    }

    private inner class BootstrapperImpl : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                dispatch(Action.LoadingRecipes)
                getRecipesUseCase.invoke()
                    .retryWhen { cause, _ ->
                        dispatch(Action.LoadingError(cause.message ?: "Unknown error"))
                        delay(5000)
                        dispatch(Action.LoadingRecipes)
                        return@retryWhen true
                    }
                    .collect {
                        dispatch(Action.LoadedRecipes(it))
                    }
            }

        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                is Intent.ToggleFavorite -> {
                    scope.launch {
                        try {
                            toggleFavoriteUseCase(intent.recipe)
                            dispatch(Msg.FavoriteToggled(intent.recipe))
                        } catch (e: Exception) {
                            dispatch(Msg.Error(e.message ?: "Unknown error"))
                        }
                    }
                }

                is Intent.RecipeClick -> {
                    publish(Label.RecipeClicked(intent.recipeId))
                }
            }
        }

        override fun executeAction(action: Action, getState: () -> State) {
            when (action) {
                is Action.LoadingRecipes -> {
                    dispatch(Msg.LoadingStarted)
                }

                is Action.LoadedRecipes -> {
                    dispatch(Msg.RecipesLoaded(action.recipes))
                }

                is Action.LoadingError -> {
                    dispatch(Msg.Error(action.message))
                }

                is Action.RecipeClick -> {
                    publish(Label.RecipeClicked(action.recipeId))
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State = when (msg) {
            is Msg.LoadingStarted -> State.Loading
            is Msg.RecipesLoaded -> State.Loaded(msg.recipes)
            is Msg.Error -> State.Error(msg.message)
            is Msg.FavoriteToggled -> this
        }
    }
}
