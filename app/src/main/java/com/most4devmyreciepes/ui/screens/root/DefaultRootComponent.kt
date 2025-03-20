package com.most4devmyreciepes.ui.screens.root

import android.os.Parcelable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.most4devmyreciepes.ui.screens.details.DefaultDetailsComponent
import com.most4devmyreciepes.ui.screens.favorite.DefaultFavoriteComponent
import com.most4devmyreciepes.ui.screens.home.DefaultHomeComponent
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.parcelize.Parcelize

class DefaultRootComponent @AssistedInject constructor(
    @Assisted("componentContext") private val componentContext: ComponentContext,
    private val homeComponentFactory: DefaultHomeComponent.Factory,
    private val favouriteComponentFactory: DefaultFavoriteComponent.Factory,
    private val detailsComponentFactory: DefaultDetailsComponent.Factory,
) : RootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    override val stack: Value<ChildStack<*, RootComponent.Child>> = childStack(
        source = navigation,
        initialConfiguration = Config.Home,
        handleBackButton = true,
        childFactory = ::child
    )

    private fun child(
        config: Config,
        componentContext: ComponentContext,
    ): RootComponent.Child {
        return when (config) {
            is Config.Home -> {
                val component = homeComponentFactory.create(
                    componentContext = componentContext,
                    onClickToRecipe = {
                        navigation.bringToFront(Config.DetailsRecipe(it))
                    },
                )
                RootComponent.Child.Home(component)
            }

            is Config.Favorites -> {
                val component = favouriteComponentFactory.create(
                    componentContext = componentContext,
                    onClickToRecipe = {
                        navigation.bringToFront(Config.DetailsRecipe(it))
                    },
                )
                RootComponent.Child.Favorite(component)
            }

            is Config.DetailsRecipe -> {
                val component = detailsComponentFactory.create(
                    recipeId = config.recipeId,
                    componentContext = componentContext,
                    onBackClicked = {
                        navigation.pop()
                    },
                )
                RootComponent.Child.Details(component)
            }
        }
    }

    override fun onTabSelected(index: Int) {
        when (index) {
            0 -> navigation.bringToFront(Config.Home)
            1 -> navigation.bringToFront(Config.Favorites)
        }
    }

    sealed interface Config : Parcelable {

        @Parcelize
        data object Home : Config

        @Parcelize
        data object Favorites : Config

        @Parcelize
        data class DetailsRecipe(val recipeId: Long) : Config

    }

    @AssistedFactory
    interface Factory {

        fun create(
            @Assisted("componentContext") componentContext: ComponentContext,
        ): DefaultRootComponent

    }

} 