package eu.peernetwork.app.ui.home

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import eu.peernetwork.app.BuildConfig
import eu.peernetwork.app.ui.settings.SettingsEvent
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.compose.DesignError
import eu.peernetwork.core.ui.design.material.DesignPage
import eu.peernetwork.core.ui.design.material.DesignPageHeader
import eu.peernetwork.core.ui.design.luna.DesignStream
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.core.ui.design.luna.DesignStreamState
import eu.peernetwork.core.ui.extension.attach
import eu.peernetwork.core.ui.extension.attachIfNecessary
import eu.peernetwork.core.ui.extension.navigateIfNecessary
import eu.peernetwork.core.ui.factory.UiViewModelStore
import eu.peernetwork.wallet.ui.reward.RewardScreen
import eu.peernetwork.social.ui.feedback.FeedbackPopup

@Composable
fun HomeScreen(
    route: String? = null,
    isOnboarded: Boolean,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onOnboard: (Boolean) -> Unit
) {
    val viewModelStore = remember { UiViewModelStore.Delegate() }
    val context = LocalContext.current
    val handleOnOnboard by rememberUpdatedState(onOnboard)
    val component = remember {
        provider.builder(Home.Builder::class.java).build(context, object : SettingsEvent {
            override fun invoke(event: SettingsEvent.Event) {
                handleOnOnboard(false)
            }
        })
    }
    val viewModel = viewModel(
        modelClass = HomeViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val derivedState = remember { derivedStateOf {
        when(state) {
            HomeViewModel.State.Empty -> DesignStreamState.Default
            HomeViewModel.State.Loading -> DesignStreamState.Loading
            is HomeViewModel.State.Success -> {
                val data = (state as HomeViewModel.State.Success)
                DesignStreamState.Success(data)
            }
            is HomeViewModel.State.Error -> {
                DesignStreamState.Error((state as HomeViewModel.State.Error).error)
            }
        }
    } }
    DesignStream(
        state = derivedState,
        modifier = Modifier.fillMaxSize(),
        loading = { HomeSkeleton() },
        error = {
            DesignError(
                onRefresh = { viewModel() },
                error = it.value,
                resource = component.resource()
            )
        }
    ) { data ->
        val controller = rememberNavController()
        val navigationState = rememberSaveable { mutableIntStateOf(data.value.lastVisitedPage) }
        val startDestination = remember { HomeRoute.get(navigationState.intValue).path }
        val navBackStackEntry by controller.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        HomeScreen(
            start = navigationState,
            options = { RewardScreen(
                provider = component,
                viewModelStoreOwner = viewModelStore.get(data.value.account.id)
            ) },
            onClick = {
                viewModel.lastVisited(it)
                navigationState.intValue = it
                controller.attachIfNecessary(HomeRoute.get(it).path)
            },
            onExplore = {
                if (currentRoute == HomeRoute.Explore.path) {
                    controller.navigateIfNecessary(HomeRoute.Home.path)
                } else if (currentRoute == HomeRoute.Home.path) {
                    controller.navigateIfNecessary(HomeRoute.Explore.path)
                } else {
                    viewModel.lastVisited(0)
                    navigationState.intValue = 0
                    controller.attach(HomeRoute.Home.path)
                }
            },
            isExploreActive = currentRoute == HomeRoute.Explore.path
        ) { state ->
            HomeNavigation(
                account = data.value.account,
                startDestination = route ?: startDestination,
                navController = controller,
                component = component,
                viewModelStore = viewModelStore,
                onExplore = { controller.navigateIfNecessary(HomeRoute.Explore.path) }
            ) {
                viewModel.lastVisited(0)
                navigationState.intValue = 0
                controller.attach(HomeRoute.Home.path)
            }
        }
        BackHandler(enabled = currentRoute != HomeRoute.Home.path) {
            viewModel.lastVisited(0)
            navigationState.intValue = 0
            controller.attachIfNecessary(HomeRoute.Home.path)
        }
        FeedbackPopup(
            appPackage = BuildConfig.APPLICATION_ID,
            provider = component,
            viewModelStoreOwner = viewModelStore.get("FeedbackPopup")
        )
        LaunchedEffect(data.value) {
            if (data.value.preference.flags.isEmpty() && !isOnboarded) {
                handleOnOnboard(true)
            }
        }
    }
    LaunchedEffect(Unit) {
        if (derivedState.value is DesignStreamState.Default) {
            viewModel()
        }
    }
    DisposableEffect(Unit) { onDispose { viewModelStore.clear() } }
}

@Composable
fun HomeScreen(
    start: State<Int>,
    options: @Composable () -> Unit,
    onClick: (Int) -> Unit,
    onExplore: () -> Unit,
    isExploreActive: Boolean,
    content: @Composable (State<Float>) -> Unit
) {
    val updatedContent by rememberUpdatedState(content)
    val handleOnClick by rememberUpdatedState(onClick)
    val handleOnExplore by rememberUpdatedState(onExplore)
    DesignPage(
        header = {
            DesignPageHeader(
                options = options,
                action = {
                    IconButton(onClick = {
                        handleOnExplore()
                    }) {
                        Icon(
                            painter = painterResource(id = if (isExploreActive) {
                                HomeRoute.Explore.activeIcon
                            } else {
                                HomeRoute.Explore.icon
                            }),
                            contentDescription = stringResource(id = HomeRoute.Explore.icon),
                            modifier = Modifier.size(19.dp)
                        )
                    }
                },
                modifier = Modifier.padding(top = 8.dp)
            ) },
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

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewHomeScreen() {
    PeerTheme {
        HomeScreen(
            start = remember { mutableIntStateOf(0) },
            options = {},
            onClick = {},
            onExplore = {},
            isExploreActive = false
        ) { state ->
            Text(
                text = "",
                textAlign = TextAlign.Center
            )
        }
    }
}
