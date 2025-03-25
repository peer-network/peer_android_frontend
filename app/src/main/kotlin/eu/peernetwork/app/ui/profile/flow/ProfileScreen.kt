package eu.peernetwork.app.ui.profile.flow

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import eu.peernetwork.app.ui.profile.preview.ProfilePreviewScreen
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.compose.DesignToolbarTitle
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.extension.navigateIfNecessary
import eu.peernetwork.user.ui.user.settings.UserSettingsScreen

@Composable
fun ProfileScreen(
    title: MutableState<DesignToolbarTitle>,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
) {
    val context = LocalContext.current
    val controller = rememberNavController()
    val component = remember {
        provider.builder(Profile.Builder::class.java).build(context)
    }
    NavHost(navController = controller, startDestination = "profile") {
        composable("profile") {
            ProfilePreviewScreen({
                controller.navigateIfNecessary("settings")
            }, component, viewModelStoreOwner)
            LaunchedEffect(Unit) {
                title.value = DesignToolbarTitle(R.string.profile_label)
            }
        }
        composable("settings") {
            UserSettingsScreen(component, viewModelStoreOwner)
            LaunchedEffect(Unit) {
                title.value = DesignToolbarTitle(R.string.settings_label)
            }
        }
    }
}
