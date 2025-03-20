package com.most4devmyreciepes.ui.screens.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.most4devmyreciepes.ui.screens.details.DetailsComponent
import com.most4devmyreciepes.ui.screens.favorite.FavoriteComponent
import com.most4devmyreciepes.ui.screens.home.HomeComponent

interface RootComponent {

    val stack: Value<ChildStack<*, Child>>

    fun onTabSelected(index: Int)

    sealed interface Child {
        data class Home(val component: HomeComponent) : Child
        data class Favorite(val component: FavoriteComponent) : Child
        data class Details(val component: DetailsComponent) : Child
    }

}