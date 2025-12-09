package eu.peernetwork.app.ui.screen

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import eu.peernetwork.core.ui.component.UiComponentProvider

fun NavGraphBuilder.screen(
    route: String,
    isModal: Boolean = false,
    expanded: Boolean = false,
    provider: UiComponentProvider,
    onCancel: () -> Unit = {},
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) {
    composable(
        route = route,
        arguments = arguments,
        deepLinks = deepLinks,
    ) {
        ScreenScaffold(
            isModal = isModal,
            expanded = expanded,
            onCancel = onCancel,
            provider = provider,
            viewModelStoreOwner = it
        ) { content( it) }
    }
}
