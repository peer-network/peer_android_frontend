package eu.peernetwork.app.ui.home

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.app.ui.feed.FeedScreen
import eu.peernetwork.app.ui.profile.flow.ProfileScreen
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.compose.DesignToolbarTitle
import eu.peernetwork.user.ui.user.point.UserPointScreen

@Composable
fun HomeScreen(
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Home.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = HomeViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val controller = rememberNavController()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val titleState = remember { mutableStateOf(DesignToolbarTitle(HomeRoute.get(state.page).label)) }
    val navigationState = rememberSaveable { mutableIntStateOf(state.page) }
    HomeScaffold(
        header = { HomeHeader(titleState) { UserPointScreen(component, viewModelStoreOwner) } },
        footer = { HomeFooter(navigationState) }
    ) {
        HomeNavigation(
            state = navigationState,
            onNavigate = { viewModel.lastVisited(it) },
            navController = controller
        ) {
            when(it) {
                is HomeRoute.Home -> FeedScreen(titleState, component, viewModelStoreOwner)
                is HomeRoute.Profile -> ProfileScreen(
                    titleState,
                    component,
                    viewModelStoreOwner
                )
                else -> Box(modifier = Modifier.fillMaxSize()) {
                    LaunchedEffect(Unit) {
                        titleState.value = DesignToolbarTitle(it.label)
                    }
                }
            }
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewHomeScreen() {
    PeerTheme {
        HomeScaffold(
            header = { HomeHeader(remember {
                mutableStateOf(DesignToolbarTitle(R.string.home_label, {}))
            }) { } },
            footer = { HomeFooter(remember { mutableIntStateOf(0) }) }
        ) {
            Text(
                text = "",
                textAlign = TextAlign.Center
            )
        }
    }
}
