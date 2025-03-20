package com.most4devmyreciepes.ui.screens.root

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import com.arkivanov.decompose.extensions.compose.jetpack.subscribeAsState
import com.most4devmyreciepes.R
import com.most4devmyreciepes.ui.screens.details.DetailsContent
import com.most4devmyreciepes.ui.screens.favorite.FavoriteContent
import com.most4devmyreciepes.ui.screens.home.HomeContent

@Composable
fun RootContent(
    component: RootComponent,
    modifier: Modifier = Modifier,
) {

    val stack = component.stack.subscribeAsState()

    Scaffold(
        bottomBar = {
            NavigationBar {
                val titleHome = stringResource(R.string.nav_home)
                val titleFavourite = stringResource(R.string.nav_favourite)

                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = titleHome) },
                    label = { Text(titleHome) },
                    selected = stack.value.active.instance is RootComponent.Child.Home,
                    onClick = { component.onTabSelected(0) }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Favorite, contentDescription = titleFavourite) },
                    label = { Text(titleFavourite) },
                    selected = stack.value.active.instance is RootComponent.Child.Favorite,
                    onClick = { component.onTabSelected(1) }
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Children(stack.value) { child ->
                when (val instance = child.instance) {
                    is RootComponent.Child.Home -> HomeContent(instance.component)
                    is RootComponent.Child.Favorite -> FavoriteContent(instance.component)
                    is RootComponent.Child.Details -> DetailsContent(instance.component)
                }
            }
        }
    }
}