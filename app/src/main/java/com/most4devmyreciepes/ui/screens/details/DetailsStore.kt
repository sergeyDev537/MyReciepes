package com.most4devmyreciepes.ui.screens.details

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.most4devmyreciepes.domain.entity.Recipe
import com.most4devmyreciepes.domain.usecases.GetRecipeByIdUseCase
import com.most4devmyreciepes.domain.usecases.ToggleFavoriteUseCase
import com.most4devmyreciepes.ui.screens.details.DetailsStore.Intent
import com.most4devmyreciepes.ui.screens.details.DetailsStore.Label
import com.most4devmyreciepes.ui.screens.details.DetailsStore.State
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.launch
import javax.inject.Inject

interface DetailsStore : Store<Intent, State, Label> {

    sealed interface Intent {
        data class ToggleFavorite(val recipe: Recipe) : Intent
        data object BackClick : Intent
    }

    sealed interface State {
        data object Initial : State
        data object Loading : State
        data class Loaded(val recipe: Recipe) : State
        data class Error(val message: String) : State
    }

    sealed interface Label {
        data object BackClick : Label
    }
}

class DetailsStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val getRecipeByIdUseCase: GetRecipeByIdUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
) {

    fun create(recipeId: Long): DetailsStore =
        object : DetailsStore, Store<Intent, State, Label> by storeFactory.create(
            name = "DetailsStore",
            initialState = State.Initial,
            bootstrapper = BootstrapperImpl(recipeId = recipeId),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {
        data object LoadingRecipe : Action
        data class LoadedRecipe(val recipe: Recipe) : Action
        data class LoadingError(val message: String) : Action
    }

    private sealed interface Msg {
        data object LoadingStarted : Msg
        data class RecipeLoaded(val recipe: Recipe) : Msg
        data class Error(val message: String) : Msg
        data class FavoriteToggled(val recipe: Recipe) : Msg
    }

    private inner class BootstrapperImpl(val recipeId: Long) : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                dispatch(Action.LoadingRecipe)
                getRecipeByIdUseCase.invoke(id = recipeId)
                    .retryWhen { cause, _ ->
                        dispatch(Action.LoadingError(cause.message ?: "Unknown error"))
                        delay(5000)
                        dispatch(Action.LoadingRecipe)
                        return@retryWhen true
                    }
                    .collect { recipe ->
                        if (recipe != null) {
                            dispatch(Action.LoadedRecipe(recipe))
                        } else {
                            dispatch(Action.LoadingError("Unknown error"))
                        }
                    }
            }
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {
        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                is Intent.BackClick -> {
                    publish(Label.BackClick)
                }

                is Intent.ToggleFavorite -> {
                    scope.launch {
                        toggleFavoriteUseCase.invoke(intent.recipe)
                    }
                    dispatch(Msg.FavoriteToggled(intent.recipe))
                }
            }
        }

        override fun executeAction(action: Action, getState: () -> State) {
            when (action) {
                is Action.LoadingRecipe -> {
                    dispatch(Msg.LoadingStarted)
                }

                is Action.LoadedRecipe -> {
                    dispatch(Msg.RecipeLoaded(action.recipe))
                }

                is Action.LoadingError -> {
                    dispatch(Msg.Error(action.message))
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State =
            when (msg) {
                is Msg.LoadingStarted -> State.Loading
                is Msg.RecipeLoaded -> State.Loaded(msg.recipe)
                is Msg.Error -> State.Error(msg.message)
                is Msg.FavoriteToggled -> this
            }
    }
}
