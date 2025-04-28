package eu.peernetwork.app.ui.home

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import eu.peernetwork.blog.ui.creator.CreatorScreen
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.app.ui.feed.FeedScreen
import eu.peernetwork.app.ui.profile.ProfileScreen
import eu.peernetwork.blog.ui.point.PointScreen
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.annotation.UiViewModel
import eu.peernetwork.core.ui.design.compose.DesignToolbarTitle
import eu.peernetwork.core.ui.design.component.DesignStatefulScaffold
import eu.peernetwork.core.ui.design.component.DesignStatefulScaffoldState
import eu.peernetwork.social.ui.search.SearchScreen
import eu.peernetwork.wallet.ui.overview.OverviewScreen

@Composable
fun HomeScreen(provider: UiComponentProvider) {
    val owner = remember { UiViewModel.Owner() }
    val context = LocalContext.current
    val component = remember {
        provider.builder(Home.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = HomeViewModel::class.java,
        viewModelStoreOwner = owner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val derivedState = remember { derivedStateOf {
        when(state) {
            HomeViewModel.State.Empty -> DesignStatefulScaffoldState.Empty
            HomeViewModel.State.Loading -> DesignStatefulScaffoldState.Loading
            is HomeViewModel.State.Success -> {
                val data = (state as HomeViewModel.State.Success)
                DesignStatefulScaffoldState.Success(Pair(data.userId, data.lastVisitedPage))
            }
            is HomeViewModel.State.Error -> {
                DesignStatefulScaffoldState.Error((state as HomeViewModel.State.Error).error)
            }
        }
    } }
    DesignStatefulScaffold<Pair<String, Int>>(
        state = derivedState,
        onRefresh = { viewModel() },
        modifier = Modifier.fillMaxSize()
    ) { data ->
        val title = remember {
            mutableStateOf(DesignToolbarTitle(HomeRoute.get(data.second).label))
        }
        HomeScreen(
            title = title,
            index = data.second,
            onNavigate = { viewModel.lastVisited(it) },
            options = { PointScreen(component, owner) }
        ) { state, route ->
            when(route) {
                is HomeRoute.Home -> FeedScreen(data.first, title, component, owner)
                is HomeRoute.Profile -> ProfileScreen(
                    data.first,
                    title,
                    component,
                    owner
                )
                is HomeRoute.Add -> CreatorScreen(title, component, owner)
                is HomeRoute.Wallet -> OverviewScreen(title, component, owner)
                is HomeRoute.Search -> SearchScreen(title, component, owner)
                else -> Box(modifier = Modifier.fillMaxSize()
                    .verticalScroll(rememberScrollState())) {
                    LaunchedEffect(Unit) {
                        title.value = DesignToolbarTitle(route.label)
                    }
                }
            }
        }
    }
}

@Composable
fun HomeScreen(
    title: MutableState<DesignToolbarTitle>,
    index: Int,
    onNavigate: (Int) -> Unit,
    options: @Composable () -> Unit,
    content: @Composable (State<Float>, HomeRoute) -> Unit
) {
    val controller = rememberNavController()
    val navigationState = rememberSaveable { mutableIntStateOf(index) }
    HomeScaffold(
        header = { HomeHeader(title, options = options) },
        footer = { HomeFooter(navigationState) }
    ) { state ->
        HomeNavigation(
            state = navigationState,
            onNavigate = onNavigate,
            navController = controller,
            content = { content(state, it) }
        )
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewHomeScreen() {
    PeerTheme {
        HomeScreen(
            title = remember { mutableStateOf(DesignToolbarTitle(R.string.home_label) {}) },
            index = 0,
            onNavigate = {},
            options = {}
        ) { state, route ->
            Text(
                text = "",
                textAlign = TextAlign.Center
            )
        }
    }
}
