package eu.peernetwork.social.ui.member

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.component.DesignRefreshableScaffold
import eu.peernetwork.core.ui.design.component.DesignStatefulScaffoldState
import eu.peernetwork.core.ui.design.compose.DesignScaffold
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.social.ui.connection.ConnectionScreen
import eu.peernetwork.social.ui.connection.ConnectionStatus
import eu.peernetwork.social.ui.renderder.BlogRenderer
import eu.peernetwork.social.ui.renderder.UserRenderer

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun MemberScreen(
    id: String,
    limit: Int,
    onSettings: () -> Unit = {},
    onMentionClick: (String) -> Unit = {},
    onHashtagClick: (String) -> Unit = {},
    imageOnClick: (String) -> Unit = {},
    photoState: LazyListState = rememberLazyListState(),
    videoState: LazyListState = rememberLazyListState(),
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Member.Builder::class.java).build(context)
    }
    val handleImageOnClick by rememberUpdatedState(imageOnClick)
    val lastUpdated = rememberSaveable { mutableLongStateOf(System.currentTimeMillis()) }
    var connection = remember { mutableStateOf<ConnectionStatus?>(null) }
    val showSheet = remember { mutableStateOf(false) }
    ConnectionScreen(
        provider = component,
        viewModelStoreOwner = viewModelStoreOwner
    ) { controller ->
        val connectionState by controller.observe().collectAsStateWithLifecycle()
        MemberScreen(
            onRefresh = { lastUpdated.longValue = System.currentTimeMillis() },
            header = { scrollState ->
                component.userRenderer()(
                    modifier = Modifier,
                    UserRenderer.Spec(
                        id,
                        lastUpdated,
                        viewModelStoreOwner,
                        onSettings,
                        {
                            ConnectionScreen(
                                isFollowing = connectionState.getOrDefault(id, it.first),
                                isFollowed = it.second,
                                onClick = { follow -> controller.invoke(id, !follow) }
                            )
                        },
                        { sheetType ->
                            connection.value = when (sheetType) {
                                0 -> ConnectionStatus.FOLLOWER
                                1 -> ConnectionStatus.FOLLOWING
                                2 -> ConnectionStatus.PEER
                                else -> null
                            }
                            showSheet.value = connection.value != null
                        }
                    )
                )
            },
        ) {
            component.blogRenderer()(
                modifier = Modifier,
                BlogRenderer.Spec(
                    id,
                    lastUpdated,
                    limit,
                    BlogRenderer.Type.UNSPECIFIED,
                    viewModelStoreOwner,
                    onMentionClick,
                    onHashtagClick,
                    imageOnClick,
                    photoState,
                    videoState
                )
            )
        }
        MemberSheet(
            id,
            showSheet,
            limit,
            connection,
            component,
            viewModelStoreOwner
        ) { handleImageOnClick(it.id) }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun MemberScreen(
    modifier: Modifier = Modifier,
    onRefresh: () -> Unit = {},
    header: @Composable (State<Float>) -> Unit,
    content: @Composable () -> Unit
) {
    val state = remember { mutableStateOf(DesignStatefulScaffoldState.Success(Unit)) }
    val updatedHeader by rememberUpdatedState(header)
    val updatedContent by rememberUpdatedState(content)
    DesignRefreshableScaffold<Unit>(
        state = state,
        modifier = Modifier.fillMaxSize(),
        onRefresh = onRefresh
    ) {
        DesignScaffold(
            modifier = modifier.fillMaxSize(),
            header = updatedHeader,
        ) { state -> updatedContent() }
    }
}

fun Pair<Boolean, Boolean>.status(): ConnectionStatus {
    return if (first && second) {
        ConnectionStatus.PEER
    } else if (first) {
        ConnectionStatus.FOLLOWING
    } else {
        ConnectionStatus.FOLLOWER
    }
}
