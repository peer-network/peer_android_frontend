package eu.peernetwork.app.ui.home

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import eu.peernetwork.app.BuildConfig
import eu.peernetwork.app.ui.onboarding.OnboardingScreen
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.compose.DesignPage
import eu.peernetwork.core.ui.design.compose.DesignPageHeader
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.core.ui.design.compose.DesignSceneState
import eu.peernetwork.core.ui.extension.attach
import eu.peernetwork.core.ui.extension.attachIfNecessary
import eu.peernetwork.core.ui.extension.navigateIfNecessary
import eu.peernetwork.core.ui.factory.UiViewModelStore
import eu.peernetwork.wallet.ui.reward.RewardScreen
import eu.peernetwork.social.ui.feedback.FeedbackPopup

@Composable
fun HomeScreen(provider: UiComponentProvider) {
    val viewModelStore = remember { UiViewModelStore.Delegate() }
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
            HomeViewModel.State.Empty -> DesignSceneState.Default
            HomeViewModel.State.Loading -> DesignSceneState.Loading
            is HomeViewModel.State.Success -> {
                val data = (state as HomeViewModel.State.Success)
                DesignSceneState.Success(data)
            }
            is HomeViewModel.State.Error -> {
                DesignSceneState.Error((state as HomeViewModel.State.Error).error)
            }
        }
    } }
    HomeScaffold(
        state = derivedState,
        resource = component.resource(),
        onRefresh = { viewModel() },
        onboarding = {
            OnboardingScreen(
                preference = it.preference,
                provider = component,
                viewModelStoreOwner = viewModelStore.get(it.userId),
            ) { preference -> viewModel(preference) }
        }
    ) { data ->
        val controller = rememberNavController()
        val navigationState = rememberSaveable { mutableIntStateOf(data.lastVisitedPage) }
        val startDestination = remember { HomeRoute.get(navigationState.intValue).path }
        val navBackStackEntry by controller.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        val isExploreActive = currentRoute == HomeRoute.Explore.path
        val toggleExplore: () -> Unit = {
            if (isExploreActive) {
                viewModel.lastVisited(0)
                navigationState.intValue = 0
                val popped = controller.popBackStack(HomeRoute.Home.path, false)
                if (!popped || controller.currentDestination?.route != HomeRoute.Home.path) {
                    controller.attachIfNecessary(HomeRoute.Home.path)
                }
            } else {
                controller.navigateIfNecessary(HomeRoute.Explore.path)
            }
        }

        HomeScreen(
            start = navigationState,
            options = { RewardScreen(
                provider = component,
                viewModelStoreOwner = viewModelStore.get(data.userId)
            ) },
            onClick = {
                viewModel.lastVisited(it)
                navigationState.intValue = it
                controller.attachIfNecessary(HomeRoute.get(it).path)
            },
            onExplore = toggleExplore,
            isExploreActive = isExploreActive
        ) { stateProgress ->
            HomeNavigation(
                id = data.userId,
                startDestination = startDestination,
                navController = controller,
                component = component,
                viewModelStore = viewModelStore,
                onExplore = toggleExplore
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
    }
    FeedbackPopup(
        BuildConfig.APPLICATION_ID,
        component,
        viewModelStore.get("FeedbackPopup")
    )
    LaunchedEffect(Unit) { viewModel() }
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
