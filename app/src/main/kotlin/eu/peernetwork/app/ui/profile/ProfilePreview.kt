package eu.peernetwork.app.ui.profile

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import eu.peernetwork.app.extension.navigateToTagSearch
import eu.peernetwork.app.extension.navigateToUsernameSearch
import eu.peernetwork.blog.domain.usecase.PostUsecase
import eu.peernetwork.blog.ui.event.UiPostListener
import eu.peernetwork.blog.ui.article.ArticleScreen
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.design.compose.DesignRefreshableScaffold
import eu.peernetwork.core.ui.design.compose.DesignStatefulScaffoldState
import eu.peernetwork.core.ui.design.material.DesignScaffold
import eu.peernetwork.core.ui.design.luna.DesignTab
import eu.peernetwork.core.ui.design.material.DesignTitle
import eu.peernetwork.core.ui.design.material.DesignTitleBarHost
import eu.peernetwork.core.ui.extension.navigateIfNecessary
import eu.peernetwork.media.core.model.UiMimeType
import eu.peernetwork.social.ui.connection.ConnectionButton
import eu.peernetwork.social.ui.connection.ConnectionScreen
import eu.peernetwork.social.ui.connection.ConnectionStatus
import eu.peernetwork.user.ui.user.UserMetric
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
    postState: LazyListState = rememberLazyListState(),
    mediaState: LazyListState = rememberLazyListState(),
    component: Profile.Component,
    viewModelStoreOwner: ViewModelStoreOwner,
    controller: NavHostController,
) {
    val requireUpdate = rememberSaveable { mutableStateOf(false) }
    val requirePostUpdate = rememberSaveable { mutableStateOf(false) }
    val connection = remember { mutableStateOf<ConnectionStatus?>(null) }
    val showSheet = remember { mutableStateOf(false) }
    val coroutine = rememberCoroutineScope()
    val enable =  remember { derivedStateOf { state.value == ProfileOverlayState.Empty } }
    val pageState = rememberPagerState(
        pageCount = { UiMimeType.TYPES.size },
        initialPage = 0
    )
    ConnectionScreen(
        provider = component,
        viewModelStoreOwner = viewModelStoreOwner
    ) { connectionController ->
        val connectionState by connectionController.value.observe().collectAsStateWithLifecycle()
        val event = remember {
            object : UiPostListener {
                override fun invoke(event: UiPostListener.Event) {
                    when(event) {
                        is UiPostListener.Event.Mention -> {
                            controller.navigateToUsernameSearch(event.username)
                        }
                        is UiPostListener.Event.Hashtag -> {
                            controller.navigateToTagSearch(event.tag)
                        }
                        is UiPostListener.Event.Author -> {
                            controller.navigateIfNecessary("profile/${event.id}")
                        }
                        is UiPostListener.Event.Post -> {
                            state.value = ProfileOverlayState.Photo(
                                id = event.id,
                                position = event.position,
                                page = pageState.currentPage
                            )
                        }
                    }
                }
            }
        }
        ProfilePreview(
            pageState = pageState,
            onRefresh = {
                requireUpdate.value = true
                requirePostUpdate.value = true
            },
            header = { scrollState ->
                UserScreen(
                    id = id,
                    requireUpdate = requireUpdate,
                    connection = {
                        ConnectionButton(
                            isFollowed = it.second,
                            isFollowing = connectionState.getOrDefault(
                                key = id,
                                defaultValue = it.first
                            ),
                            onClick = { follow ->
                                connectionController.value.invoke(id, !follow)
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    onClick = { sheetType ->
                        connection.value = when (sheetType) {
                            UserMetric.FOLLOWER -> ConnectionStatus.FOLLOWER
                            UserMetric.FOLLOWING -> ConnectionStatus.FOLLOWING
                            UserMetric.PEER -> ConnectionStatus.PEER
                            else -> null
                        }
                        showSheet.value = connection.value != null
                    },
                    onSettings = onSettings,
                    onBoost = {},
                    provider = component,
                    viewModelStoreOwner = viewModelStoreOwner,
                    modifier = Modifier.padding(bottom = 8.dp)
                        .padding(end = 16.dp, start = 24.dp)
                )
            },
        ) {
            ArticleScreen(
                author = id,
                types = if (it == 0) {
                    PostUsecase.POST
                } else {
                    PostUsecase.MEDIA
                },
                status = enable,
                postLimit = limit,
                requireUpdate = requirePostUpdate,
                provider = component,
                viewModelStoreOwner = viewModelStoreOwner,
                event = event,
                listState =  if (it == 0) {
                    postState
                } else {
                    mediaState
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
        ) { controller.navigateIfNecessary("profile/${it.id}") }
    }
    DesignTitleBarHost("ProfileScreen$id", {
        coroutine.launch {
            if (pageState.currentPage == 0) {
                postState.animateScrollToItem(0)
            } else {
                mediaState.animateScrollToItem(0)
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
    pageState: PagerState,
    modifier: Modifier = Modifier,
    onRefresh: () -> Unit = {},
    header: @Composable (State<Float>) -> Unit,
    content: @Composable (Int) -> Unit
) {
    val state = remember {
        mutableStateOf(DesignStatefulScaffoldState.Success(Unit))
    }
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
        ) {
            DesignTab(
                pageState,
                modifier = Modifier.padding(top = 2.dp)
            ) { index ->
                UiMimeType.get(index)?.let {
                    Icon(
                        painter = painterResource(id = it.id),
                        contentDescription = it.label?.let { stringResource(it) },
                        tint = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.padding(vertical = 6.dp).size(18.dp)
                    )
                }
            }
            HorizontalPager(
                state = pageState,
                verticalAlignment = Alignment.Top,
            ) { page -> updatedContent(page) }
        }
    }
}
