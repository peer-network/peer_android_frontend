package eu.peernetwork.app.ui.profile

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import eu.peernetwork.app.extension.navigateToTagSearch
import eu.peernetwork.app.extension.navigateToUsernameSearch
import eu.peernetwork.blog.ui.event.UiPostEvent
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.design.component.DesignRefreshableScaffold
import eu.peernetwork.core.ui.design.component.DesignStatefulScaffoldState
import eu.peernetwork.core.ui.design.compose.DesignScaffold
import eu.peernetwork.core.ui.design.compose.DesignTitle
import eu.peernetwork.core.ui.design.compose.DesignTitleBarHost
import eu.peernetwork.core.ui.extension.navigateIfNecessary
import eu.peernetwork.social.ui.connection.ConnectionScreen
import eu.peernetwork.social.ui.connection.ConnectionStatus
import eu.peernetwork.user.ui.user.UserScreen
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ProfilePreview(
    id: String,
    title: String?,
    state: MutableState<ProfileOverlayState>,
    limit: Int,
    onSettings: () -> Unit = {},
    photoState: LazyListState = rememberLazyListState(),
    videoState: LazyListState = rememberLazyListState(),
    component: Profile.Component,
    viewModelStoreOwner: ViewModelStoreOwner,
    controller: NavHostController,
) {
    val lastUpdated = rememberSaveable { mutableLongStateOf(System.currentTimeMillis()) }
    val connection = remember { mutableStateOf<ConnectionStatus?>(null) }
    val showSheet = remember { mutableStateOf(false) }
    val coroutine = rememberCoroutineScope()
    var position by remember { mutableIntStateOf(0) }
    val enable =  remember { derivedStateOf { state.value == ProfileOverlayState.Empty } }
    ConnectionScreen(
        provider = component,
        viewModelStoreOwner = viewModelStoreOwner
    ) { connectionController ->
        val connectionState by connectionController.value.observe().collectAsStateWithLifecycle()
        val event = remember {
            object : UiPostEvent {
                override fun onMentionClick(username: String) = controller.navigateToUsernameSearch(username)

                override fun onHashtagClick(tag: String) = controller.navigateToTagSearch(tag)

                override fun onPostClick(id: String, position: Int) {
                    state.value = ProfileOverlayState.Photo(id, position)
                }

                override fun onVideoClick(id: String, position: Int) {
                    state.value = ProfileOverlayState.Video(id, position)
                }

                override fun onAuthorClick(id: String) = controller.navigateIfNecessary("profile/$id")
            }
        }
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
                            onClick = { follow -> connectionController.value.invoke(id, !follow) }
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
                id = id,
                enable = enable,
                lastUpdated = lastUpdated,
                limit = limit,
                provider = component,
                viewModelStoreOwner = viewModelStoreOwner,
                onNavigate = { position = it },
                event = event,
                photoState = photoState,
                videoState = videoState,
                connection =  {
                    ConnectionScreen(
                        isFollowing = connectionState.getOrDefault(
                            key = it.first,
                            defaultValue = it.second
                        ),
                        isFollowed = it.second,
                        onClick = { follow -> connectionController.value.invoke(it.first, !follow) }
                    )
                }
            )
        }
        ProfileSheet(
            id = id,
            state = showSheet,
            limit = limit,
            status = connection,
            provider = component,
            viewModelStoreOwner = viewModelStoreOwner
        ) { event.onAuthorClick(it.id) }
    }
    DesignTitleBarHost("ProfileScreen$id", {
        coroutine.launch {
            if (position == 0) {
                photoState.animateScrollToItem(0)
            } else {
                videoState.animateScrollToItem(0)
            }
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
