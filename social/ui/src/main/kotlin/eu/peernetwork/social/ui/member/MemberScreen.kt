package eu.peernetwork.social.ui.member

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.component.DesignRefreshableScaffold
import eu.peernetwork.core.ui.design.component.DesignStatefulScaffoldState
import eu.peernetwork.core.ui.design.compose.DesignScaffold
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.social.ui.renderder.BlogRenderer
import eu.peernetwork.social.ui.renderder.UserRenderer

@Composable
fun MemberScreen(
    id: String,
    limit: Int,
    type: UserRenderer.Type,
    onSettings: () -> Unit = {},
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Member.Builder::class.java).build(context)
    }
    val viewModel: MemberViewModel = viewModel(
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    var userState = remember { mutableStateOf(false) }
    var refreshing = remember { mutableStateOf(false) }

    MemberScreen(
        onRefresh = {
            refreshing.value = true
            userState.value = true
        },
        header = { scrollState ->
            FollowButton(
                viewModelStoreOwner = viewModelStoreOwner,
                provider = provider
            ) { onFollow, error, success ->
                component.userRenderer()(
                    modifier = Modifier,
                    UserRenderer.Spec(
                        id,
                        userState,
                        viewModelStoreOwner,
                        type,
                        onSettings,
                        {
                            FollowButtonStateless(
                                isFollowing = success?.followings?.get(id) ?: it.first,
                                error = error,
                                initiallyFollowedBy = it.second,
                                onClick = { onFollow(id) }
                            )
                        },
                        {}
                    )
                )
            }
        },
    ) {
        component.blogRenderer()(
            modifier = Modifier,
            BlogRenderer.Spec(
                id,
                refreshing,
                limit,
                BlogRenderer.Type.UNSPECIFIED,
                viewModelStoreOwner
            )
        )
    }
}

@Composable
fun MemberScreen(
    modifier: Modifier = Modifier,
    onRefresh: () -> Unit = {},
    header: @Composable (State<Float>) -> Unit,
    content: @Composable () -> Unit
) {
    val state = remember { mutableStateOf(DesignStatefulScaffoldState.Success(Unit)) }
    DesignRefreshableScaffold<Unit>(
        state = state,
        modifier = Modifier.fillMaxSize(),
        onRefresh = onRefresh
    ) {
        DesignScaffold(
            modifier = modifier.fillMaxSize(),
            header = header,
        ) { state -> content() }
    }
}
