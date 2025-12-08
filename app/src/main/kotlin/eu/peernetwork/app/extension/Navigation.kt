package eu.peernetwork.app.extension

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eu.peernetwork.core.ui.design.material.DesignTitleBar

fun NavGraphBuilder.route(
    route: String,
    isModal: Boolean = false,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) {
    composable(
        route = route,
        arguments = arguments,
        deepLinks = deepLinks,
    ) {
        if (isModal) {
            DesignTitleBar {
                Box {
                    content( it)
                }
            }
        } else {
            content( it)
        }
    }
}
