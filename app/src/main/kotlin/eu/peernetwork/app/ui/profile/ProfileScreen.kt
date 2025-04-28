package eu.peernetwork.app.ui.profile

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import eu.peernetwork.app.BuildConfig
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.compose.DesignRouter
import eu.peernetwork.core.ui.design.compose.DesignToolbarTitle
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.extension.navigateIfNecessary
import eu.peernetwork.social.ui.member.MemberScreen
import eu.peernetwork.social.ui.renderder.UserRenderer
import eu.peernetwork.user.ui.settings.SettingsScreen

@Composable
fun ProfileScreen(
    userId: String,
    title: MutableState<DesignToolbarTitle>,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    type: UserRenderer.Type = UserRenderer.Type.ACCOUNT,
) {
    val context = LocalContext.current
    val controller = rememberNavController()
    val component = remember {
        provider.builder(Profile.Builder::class.java).build(context)
    }
    DesignRouter(navController = controller, startDestination = "profile") {
        composable("profile") {
            MemberScreen(
                id = userId,
                limit = BuildConfig.PAGING_LIMIT,
                type = type,
                onSettings = { controller.navigateIfNecessary("settings") },
                provider = component,
                viewModelStoreOwner = viewModelStoreOwner
            )
            LaunchedEffect(Unit) {
                title.value = DesignToolbarTitle(R.string.profile_label)
            }
        }
        composable("settings") {
            SettingsScreen(component, viewModelStoreOwner)
            LaunchedEffect(Unit) {
                title.value = DesignToolbarTitle(R.string.settings_label)
            }
        }
    }
}
