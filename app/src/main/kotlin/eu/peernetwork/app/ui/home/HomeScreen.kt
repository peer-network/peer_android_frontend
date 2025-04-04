package eu.peernetwork.app.ui.home

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
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
import eu.peernetwork.app.ui.profile.core.ProfileScreen
import eu.peernetwork.blog.ui.point.UserPointScreen
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.design.compose.DesignToolbarTitle
import eu.peernetwork.core.ui.design.view.DesignStatefulContent
import eu.peernetwork.core.ui.design.view.DesignStatefulContentState

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
    val state by viewModel.state.collectAsStateWithLifecycle()
    val derivedState = remember { derivedStateOf {
        when(state) {
            HomeViewModel.State.Empty -> DesignStatefulContentState.Empty
            HomeViewModel.State.Loading -> DesignStatefulContentState.Loading
            is HomeViewModel.State.Success -> {
                val data = (state as HomeViewModel.State.Success)
                DesignStatefulContentState.Success(Pair(data.userId, data.lastVisitedPage))
            }
            is HomeViewModel.State.Error -> {
                DesignStatefulContentState.Error((state as HomeViewModel.State.Error).error)
            }
        }
    } }
    DesignStatefulContent<Pair<String, Int>>(
        state = derivedState,
        refresh = { viewModel() },
        modifier = Modifier.fillMaxSize()
    ) { data ->
        val title = remember {
            mutableStateOf(DesignToolbarTitle(HomeRoute.get(data.second).label))
        }
        HomeContainer(
            title = title,
            index = data.second,
            onNavigate = { viewModel.lastVisited(it) },
            options = { UserPointScreen(component, viewModelStoreOwner) }
        ) {
            when(it) {
                is HomeRoute.Home -> FeedScreen(title, component, viewModelStoreOwner)
                is HomeRoute.Profile -> ProfileScreen(
                    data.first,
                    title,
                    component,
                    viewModelStoreOwner
                )
                else -> Box(modifier = Modifier.fillMaxSize()) {
                    LaunchedEffect(Unit) {
                        title.value = DesignToolbarTitle(it.label)
                    }
                }
            }
        }
    }
}

@Composable
fun HomeContainer(
    title: MutableState<DesignToolbarTitle>,
    index: Int,
    onNavigate: (Int) -> Unit,
    options: @Composable () -> Unit,
    content: @Composable (HomeRoute) -> Unit
) {
    val controller = rememberNavController()
    val navigationState = rememberSaveable { mutableIntStateOf(index) }
    HomeScaffold(
        header = { HomeHeader(title, options = options) },
        footer = { HomeFooter(navigationState) }
    ) {
        HomeNavigation(
            state = navigationState,
            onNavigate = onNavigate,
            navController = controller,
            content = content
        )
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewHomeScreen() {
    PeerTheme {
        HomeScaffold(
            header = { HomeHeader(remember { mutableStateOf(DesignToolbarTitle(R.string.home_label)) }) { } },
            footer = { HomeFooter(remember { mutableIntStateOf(0) }) }
        ) {
            Text(
                text = "",
                textAlign = TextAlign.Center
            )
        }
    }
}
