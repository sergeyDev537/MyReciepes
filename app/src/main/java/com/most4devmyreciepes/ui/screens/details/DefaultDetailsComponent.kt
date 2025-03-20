package com.most4devmyreciepes.ui.screens.details

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

class DefaultDetailsComponent @AssistedInject constructor(
    private val storeFactory: DetailsStoreFactory,
    @Assisted("recipeId") private val recipeId: Long,
    @Assisted("componentContext") private val componentContext: ComponentContext,
    @Assisted("onBackClicked") private val onBackClicked: () -> Unit,
) : DetailsComponent, ComponentContext by componentContext {

    private val store = instanceKeeper.getStore { storeFactory.create(recipeId) }
    private val scope = componentScope()

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<DetailsStore.State> = store.stateFlow

    init {
        scope.launch {
            store.labels.collect {
                when (it) {
                    is DetailsStore.Label.BackClick -> {
                        onBackClicked.invoke()
                    }
                }
            }
        }
    }

    override fun onToggleFavorite(recipe: Recipe) {
        store.accept(DetailsStore.Intent.ToggleFavorite(recipe))
    }

    override fun clickBack() {
        onBackClicked.invoke()
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("recipeId") recipeId: Long,
            @Assisted("componentContext") componentContext: ComponentContext,
            @Assisted("onBackClicked") onBackClicked: () -> Unit,
        ): DefaultDetailsComponent
    }

}