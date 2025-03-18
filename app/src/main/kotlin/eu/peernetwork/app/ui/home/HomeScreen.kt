package eu.peernetwork.app.ui.home

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.extension.attachIfNecessary
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.social.ui.feed.FeedScreen

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
    LaunchedEffect(state) {
        when(state) {
            is HomeViewModel.State.Initialize -> controller.attachIfNecessary("initialize")
            is HomeViewModel.State.Ready -> {
                val page = (state as HomeViewModel.State.Ready).page
                controller.attachIfNecessary("content/$page")
            }
        }
    }
    HomeScaffold(
        header = { HomeHeader(title = { Text("Friends") },
            modifier = Modifier.height(56.dp)) },
        footer = { HomeFooter() }
    ) {
        Box(
            modifier = Modifier.padding(it),
        ) {
            NavHost(navController = controller, startDestination = "initialize") {
                composable("initialize") { }
                composable(
                    "content/{page}",
                    arguments = listOf(navArgument("page") { type = NavType.IntType })
                ) {
                    FeedScreen(
                        page = it.arguments?.getInt("page") ?: 0,
                        onNavigate = { viewModel.updateFeed(it) },
                        component,
                        viewModelStoreOwner
                    )
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
            header = { HomeHeader(title = {}) },
            footer = { HomeFooter() }
        ) {
            Text(
                text = "Hello, world!",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(it)
            )
        }
    }
}
