package eu.peernetwork.app.ui.home

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import eu.peernetwork.app.BuildConfig
import eu.peernetwork.app.ui.composer.ComposerScreen
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.app.ui.feed.FeedScreen
import eu.peernetwork.app.ui.profile.ProfileScreen
import eu.peernetwork.blog.ui.point.PointScreen
import eu.peernetwork.core.ui.design.component.DesignStatefulScaffold
import eu.peernetwork.core.ui.design.component.DesignStatefulScaffoldState
import eu.peernetwork.app.ui.search.SearchScreen
import eu.peernetwork.core.ui.design.compose.DesignTitleBar
import eu.peernetwork.core.ui.model.ViewModelState
import eu.peernetwork.messaging.ui.chat.ChatScreen
import eu.peernetwork.wallet.ui.overview.OverviewScreen

@Composable
fun HomeScreen(provider: UiComponentProvider) {
    val viewModelStore = remember { ViewModelState() }
    val context = LocalContext.current
    val component = remember {
        provider.builder(Home.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = HomeViewModel::class.java,
        viewModelStoreOwner = viewModelStore.get(Home.Builder::class.java.name),
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
        HomeScreen(
            index = data.second,
            onNavigate = { viewModel.lastVisited(it) },
            options = { PointScreen(component, viewModelStore.get(data.first)) },
            messaging = { ChatScreen(it, component, viewModelStore.get(data.first)) }
        ) { state, route ->
            when(route) {
                is HomeRoute.Home -> FeedScreen(
                    data.first,
                    BuildConfig.PAGING_LIMIT,
                    component,
                    viewModelStore,
                )
                is HomeRoute.Profile -> ProfileScreen(
                    data.first,
                    component,
                    viewModelStore,
                )
                is HomeRoute.Add -> ComposerScreen(component, viewModelStore.get(data.first))
                is HomeRoute.Wallet -> OverviewScreen(component, viewModelStore.get(data.first))
                is HomeRoute.Search -> SearchScreen(
                    id = data.first,
                    BuildConfig.PAGING_LIMIT,
                    component,
                    viewModelStore,
                )
                else -> {}
            }
        }
    }
    DisposableEffect(Unit) { onDispose { viewModelStore.clear() } }
}

@Composable
fun HomeScreen(
    index: Int,
    onNavigate: (Int) -> Unit,
    options: @Composable () -> Unit,
    messaging: @Composable (Boolean) -> Unit,
    content: @Composable (State<Float>, HomeRoute) -> Unit
) {
    val controller = rememberNavController()
    val navigationState = rememberSaveable { mutableIntStateOf(index) }
    val updatedContent by rememberUpdatedState(content)
    val updatedMessaging by rememberUpdatedState(messaging)
    val contentState = rememberPagerState(pageCount = { 2 }, initialPage = 0)
    DesignTitleBar {
        HomeScaffold(
            header = { HomeHeader(options = options, modifier = Modifier.padding(top = 8.dp)) },
            footer = {
                HomeFooter(
                    navigationState,
                    onClick = { titleBar().value?.listener?.invoke() }
                ) }
        ) { state ->
            HorizontalPager(
                state = contentState,
                verticalAlignment = Alignment.Top,
            ) { page ->
                when (page) {
                    0 -> {
                        HomeNavigation(
                            state = navigationState,
                            onNavigate = onNavigate,
                            navController = controller,
                            content = { updatedContent(state, it) }
                        )
                    }
                    1 -> { updatedMessaging(contentState.currentPage == page) }
                }
            }
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewHomeScreen() {
    PeerTheme {
        HomeScreen(
            index = 0,
            onNavigate = {},
            options = {},
            messaging = {}
        ) { state, route ->
            Text(
                text = "",
                textAlign = TextAlign.Center
            )
        }
    }
}
