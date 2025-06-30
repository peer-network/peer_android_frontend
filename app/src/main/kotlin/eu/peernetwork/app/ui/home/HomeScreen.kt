package eu.peernetwork.app.ui.home

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.core.ui.design.component.DesignStatefulScaffold
import eu.peernetwork.core.ui.design.component.DesignStatefulScaffoldState
import eu.peernetwork.core.ui.design.compose.DesignTitleBar
import eu.peernetwork.core.ui.extension.attach
import eu.peernetwork.core.ui.extension.attachIfNecessary
import eu.peernetwork.core.ui.model.ViewModelState
import eu.peernetwork.wallet.ui.reward.RewardScreen

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
        val controller = rememberNavController()
        val navigationState = rememberSaveable { mutableIntStateOf(data.second) }
        val startDestination = remember { HomeRoute.get(navigationState.intValue).path }
        val navBackStackEntry by controller.currentBackStackEntryAsState()
        val currentStack = remember(navBackStackEntry?.id) { mutableStateOf(controller.currentDestination?.route) }
        HomeScreen(
            start = navigationState,
            options = { RewardScreen(component, viewModelStore.get(data.first)) },
            onClick = {
                viewModel.lastVisited(it)
                navigationState.intValue = it
                controller.attachIfNecessary(HomeRoute.get(it).path)
            }
        ) { state ->
            HomeNavigation(
                id = data.first,
                startDestination = startDestination,
                navController = controller,
                component = component,
                viewModelStore = viewModelStore
            ) {
                viewModel.lastVisited(0)
                navigationState.intValue = 0
                controller.attach(HomeRoute.Home.path)
            }
        }
        BackHandler(enabled = currentStack.value != HomeRoute.Home.path) {
            viewModel.lastVisited(0)
            navigationState.intValue = 0
            controller.attachIfNecessary(HomeRoute.Home.path)
        }
    }
    DisposableEffect(Unit) { onDispose { viewModelStore.clear() } }
}

@Composable
fun HomeScreen(
    start: State<Int>,
    options: @Composable () -> Unit,
    onClick: (Int) -> Unit,
    content: @Composable (State<Float>) -> Unit
) {
    val updatedContent by rememberUpdatedState(content)
    val handleOnClick by rememberUpdatedState(onClick)
    DesignTitleBar {
        HomeScaffold(
            header = { HomeHeader(options = options, modifier = Modifier.padding(top = 8.dp)) },
            footer = {
                HomeFooter(
                    start,
                    onClick = { prev, next ->
                        if (prev == next) {
                            titleBar().value?.listener?.invoke()
                        } else {
                            handleOnClick(next)
                        }
                    }
                ) }
        ) { state -> updatedContent(state) }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewHomeScreen() {
    PeerTheme {
        HomeScreen(
            start = remember { mutableIntStateOf(0) },
            options = {},
            onClick = {},
        ) { state ->
            Text(
                text = "",
                textAlign = TextAlign.Center
            )
        }
    }
}
