package eu.peernetwork.social.ui.member

import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.component.DesignRefreshableScaffold
import eu.peernetwork.core.ui.design.component.DesignStatefulScaffoldState
import eu.peernetwork.core.ui.design.compose.DesignBottomSheet
import eu.peernetwork.core.ui.design.compose.DesignScaffold
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.social.ui.connection.ConnectionScreen
import eu.peernetwork.social.ui.connection.ConnectionStatus
import eu.peernetwork.social.ui.renderder.BlogRenderer
import eu.peernetwork.social.ui.renderder.UserRenderer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemberScreen(
    id: String,
    limit: Int,
    type: UserRenderer.Type,
    onSettings: () -> Unit = {},
    onMentionClick: (String) -> Unit = {},
    onHashtagClick: (String) -> Unit = {},
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Member.Builder::class.java).build(context)
    }
    val userState = remember { mutableStateOf(false) }
    val refreshing = remember { mutableStateOf(false) }
    var connection = remember { mutableStateOf<ConnectionStatus?>(null) }
    ConnectionScreen(
        provider = component,
        viewModelStoreOwner = viewModelStoreOwner
    ) { controller ->
        val connectionState by controller.observe().collectAsStateWithLifecycle()
        MemberScreen(
            onRefresh = {
                refreshing.value = true
                userState.value = true
            },
            connection = connection,
            header = { scrollState ->
                component.userRenderer()(
                    modifier = Modifier,
                    UserRenderer.Spec(
                        id,
                        userState,
                        viewModelStoreOwner,
                        type,
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
                                0 -> ConnectionStatus.FOLLOW
                                1 -> ConnectionStatus.FOLLOWING
                                2 -> ConnectionStatus.PEER
                                else -> null
                            }
                        }
                    )
                )
            },
            sheet = { MemberSheet(id, limit, it, component, viewModelStoreOwner) }
        ) {
            component.blogRenderer()(
                modifier = Modifier,
                BlogRenderer.Spec(
                    id,
                    refreshing,
                    limit,
                    BlogRenderer.Type.UNSPECIFIED,
                    viewModelStoreOwner,
                    onMentionClick,
                    onHashtagClick
                )
            )
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun MemberScreen(
    modifier: Modifier = Modifier,
    onRefresh: () -> Unit = {},
    connection: MutableState<ConnectionStatus?>,
    header: @Composable (State<Float>) -> Unit,
    sheet: @Composable (ConnectionStatus) -> Unit,
    content: @Composable () -> Unit
) {
    val state = remember { mutableStateOf(DesignStatefulScaffoldState.Success(Unit)) }
    val showSheet = remember(connection.value) { mutableStateOf(connection.value != null) }
    val updatedHeader by rememberUpdatedState(header)
    val updatedContent by rememberUpdatedState(content)
    val updatedSheet by rememberUpdatedState(sheet)
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
    DesignBottomSheet(
        onDismissRequest = { connection.value = null },
        tag = "SocialMemberConnection",
        showSheet = showSheet,
        sheetPeekHeight = 500.dp,
        modifier = Modifier
            .defaultMinSize(minHeight = 500.dp),
        content = { connection.value?.let { updatedSheet(it) } }
    )
}

fun Pair<Boolean, Boolean>.status(): ConnectionStatus {
    return if (first && second) {
        ConnectionStatus.PEER
    } else if (first) {
        ConnectionStatus.FOLLOWING
    } else {
        ConnectionStatus.FOLLOW
    }
}
