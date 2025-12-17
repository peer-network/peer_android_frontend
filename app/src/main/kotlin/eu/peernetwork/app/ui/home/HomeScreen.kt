package eu.peernetwork.app.ui.home

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import eu.peernetwork.app.BuildConfig
import eu.peernetwork.app.ui.settings.SettingsEvent
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.luna.DesignStream
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.design.luna.DesignStreamState
import eu.peernetwork.core.ui.extension.attach
import eu.peernetwork.core.ui.extension.attachIfNecessary
import eu.peernetwork.core.ui.extension.navigateIfNecessary
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
            HomeError(
                onRefresh = { viewModel() },
                error = it,
                component = component
            )
        }
    ) { data ->
        val controller = rememberNavController()
        val navigationState = rememberSaveable { mutableIntStateOf(data.value.lastVisitedPage) }
        val startDestination = remember { HomeMenu.get(navigationState.intValue).path }
        val navBackStackEntry by controller.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        HomePage(
            start = navigationState,
            options = { RewardScreen(
                provider = component,
                viewModelStoreOwner = viewModelStoreOwner
            ) },
            onClick = {
                viewModel.lastVisited(it)
                navigationState.intValue = it
                controller.attachIfNecessary(HomeMenu.get(it).path)
            },
            onExplore = {
                if (currentRoute == HomeMenu.Explore.path) {
                    controller.navigateIfNecessary(HomeMenu.Home.path)
                } else if (currentRoute == HomeMenu.Home.path) {
                    controller.navigateIfNecessary(HomeMenu.Explore.path)
                } else {
                    viewModel.lastVisited(0)
                    navigationState.intValue = 0
                    controller.attach(HomeMenu.Home.path)
                }
            },
            isExploreActive = currentRoute == HomeMenu.Explore.path
        ) { state ->
            HomeNavigation(
                account = data.value.account,
                startDestination = route ?: startDestination,
                navController = controller,
                component = component,
                viewModelStoreOwner = viewModelStoreOwner,
                onExplore = { controller.navigateIfNecessary(HomeMenu.Explore.path) }
            ) {
                viewModel.lastVisited(0)
                navigationState.intValue = 0
                controller.attach(HomeMenu.Home.path)
            }
        }
        BackHandler(enabled = currentRoute != HomeMenu.Home.path) {
            viewModel.lastVisited(0)
            navigationState.intValue = 0
            controller.attachIfNecessary(HomeMenu.Home.path)
        }
        FeedbackPopup(
            appPackage = BuildConfig.APPLICATION_ID,
            provider = component,
            viewModelStoreOwner = viewModelStoreOwner
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
}
