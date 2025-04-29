package eu.peernetwork.app.ui.search

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import eu.peernetwork.app.ui.profile.ProfileScreen
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.annotation.UiViewModel
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.component.DesignRefreshableScaffold
import eu.peernetwork.core.ui.design.component.DesignStatefulScaffoldState
import eu.peernetwork.core.ui.design.compose.DesignRouter
import eu.peernetwork.core.ui.design.compose.DesignScaffold
import eu.peernetwork.core.ui.design.compose.DesignToolbarTitle
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.extension.navigateIfNecessary
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.social.ui.renderder.UserRenderer
import eu.peernetwork.social.ui.search.member.MemberScreen

enum class SearchMode {
    USERNAME, TAG, TITLE
}

@Composable
fun SearchScreen(
    id: String,
    title: MutableState<DesignToolbarTitle>,
    postLimit: Int,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Search.Builder::class.java).build(context)
    }
    val controller = rememberNavController()
    DesignRouter(
        navController = controller,
        startDestination = "search",
    ) {
        composable("search") {
            SearchScreen(
                onRefresh = {}
            ) { mode, query ->
                if (query.text.length >= 3) {
                    MemberScreen(query, postLimit, {
                        controller.navigateIfNecessary("profile/$it")
                    }, component, viewModelStoreOwner)
                }
            }
        }
        composable(
            "profile/{id}",
            arguments = listOf(navArgument("id") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("id")
            ProfileScreen(
                userId = userId ?: id,
                title = title,
                type = if (userId == id) {
                    UserRenderer.Type.ACCOUNT
                } else {
                    userId?.let {
                        UserRenderer.Type.USER
                    } ?: UserRenderer.Type.ACCOUNT
                },
                provider = component,
                viewModelStoreOwner = if (userId == id) {
                    viewModelStoreOwner
                } else {
                    UiViewModel.Owner()
                }
            )
        }
    }
    LaunchedEffect(Unit) {
        title.value = DesignToolbarTitle(R.string.search_label)
    }
}

@Composable
fun SearchScreen(
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (SearchMode, TextFieldState) -> Unit
) {
    val state = remember { mutableStateOf(DesignStatefulScaffoldState.Success(Unit)) }
    val query = remember { TextFieldState() }
    DesignScaffold(
        modifier = modifier.fillMaxSize(),
        alwaysReturn = true,
        header = { SearchHeader(query) },
    ) { contentState ->
        DesignRefreshableScaffold<Unit>(
            state = state,
            onRefresh = onRefresh
        ) {
            content(SearchMode.USERNAME, query)
        }
    }
}

@Preview
@Composable
fun PreviewSearchScreen() {
    PeerTheme {
        SearchScreen(
            onRefresh = {}
        ) { mode, query -> }
    }
}
