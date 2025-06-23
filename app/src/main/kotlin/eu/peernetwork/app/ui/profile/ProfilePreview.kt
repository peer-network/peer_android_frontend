package eu.peernetwork.app.ui.profile

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.design.component.DesignRefreshableScaffold
import eu.peernetwork.core.ui.design.component.DesignStatefulScaffoldState
import eu.peernetwork.core.ui.design.compose.DesignScaffold
import eu.peernetwork.core.ui.design.compose.DesignTitle
import eu.peernetwork.core.ui.design.compose.DesignTitleBarHost
import eu.peernetwork.social.ui.connection.ConnectionScreen
import eu.peernetwork.social.ui.connection.ConnectionStatus
import eu.peernetwork.user.ui.user.UserScreen
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ProfilePreview(
    id: String,
    title: String?,
    limit: Int,
    onSettings: () -> Unit = {},
    onMentionClick: (String) -> Unit = {},
    onHashtagClick: (String) -> Unit = {},
    onAuthorClicked: (String) -> Unit = {},
    photoState: LazyListState = rememberLazyListState(),
    videoState: LazyListState = rememberLazyListState(),
    component: Profile.Component,
    viewModelStoreOwner: ViewModelStoreOwner,
    onPhotoClick: (String, Int) -> Unit = { id, position -> },
    onVideoClick: (String, Int) -> Unit = { id, position -> },
) {
    val handleAuthorClicked by rememberUpdatedState(onAuthorClicked)
    val lastUpdated = rememberSaveable { mutableLongStateOf(System.currentTimeMillis()) }
    var connection = remember { mutableStateOf<ConnectionStatus?>(null) }
    val showSheet = remember { mutableStateOf(false) }
    val coroutine = rememberCoroutineScope()
    ConnectionScreen(
        provider = component,
        viewModelStoreOwner = viewModelStoreOwner
    ) { controller ->
        val connectionState by controller.observe().collectAsStateWithLifecycle()
        ProfilePreview(
            onRefresh = { lastUpdated.longValue = System.currentTimeMillis() },
            header = { scrollState ->
                UserScreen(
                    id = id,
                    lastUpdated = lastUpdated,
                    onFollow = {
                        ConnectionScreen(
                            isFollowing = connectionState.getOrDefault(id, it.first),
                            isFollowed = it.second,
                            onClick = { follow -> controller.invoke(id, !follow) }
                        )
                    },
                    onClick = { sheetType ->
                        connection.value = when (sheetType) {
                            0 -> ConnectionStatus.FOLLOWER
                            1 -> ConnectionStatus.FOLLOWING
                            2 -> ConnectionStatus.PEER
                            else -> null
                        }
                        showSheet.value = connection.value != null
                    },
                    onSettings = onSettings,
                    provider = component,
                    viewModelStoreOwner = viewModelStoreOwner,
                    modifier = Modifier.Companion.padding(bottom = 8.dp)
                        .padding(end = 16.dp, start = 24.dp)
                )
            },
        ) {
            ProfileBlog(
                id,
                lastUpdated,
                limit,
                component,
                viewModelStoreOwner,
                onMentionClick,
                onHashtagClick,
                onAuthorClicked,
                onPhotoClick,
                onVideoClick,
                photoState,
                videoState
            )
        }
        ProfileSheet(
            id,
            showSheet,
            limit,
            connection,
            component,
            viewModelStoreOwner
        ) { handleAuthorClicked(it.id) }
    }
    DesignTitleBarHost("ProfileScreen$id", {
        coroutine.launch {
            photoState.animateScrollToItem(0)
            videoState.animateScrollToItem(0)
        }
    }) {
        titleBar {
            DesignTitle {
                Text(title ?: stringResource(R.string.profile_label))
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ProfilePreview(
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
