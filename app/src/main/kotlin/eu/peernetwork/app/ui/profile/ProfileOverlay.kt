package eu.peernetwork.app.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import eu.peernetwork.app.ui.feed.navigateToTagSearch
import eu.peernetwork.app.ui.feed.navigateToUsernameSearch
import eu.peernetwork.blog.ui.engagement.EngagementScreen
import eu.peernetwork.blog.ui.moderation.ModerationScreen
import eu.peernetwork.blog.ui.post.photo.PhotoOverlay
import eu.peernetwork.blog.ui.post.video.Video
import eu.peernetwork.blog.ui.post.video.VideoOverlay
import eu.peernetwork.blog.ui.post.video.VideoViewModel
import eu.peernetwork.core.ui.design.compose.DesignDialogSheet
import eu.peernetwork.core.ui.design.compose.DesignOverlayPage
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.extension.navigateIfNecessary
import eu.peernetwork.core.ui.model.ViewModelState
import eu.peernetwork.media.core.model.UiMimeType
import eu.peernetwork.social.ui.connection.ConnectionController
import eu.peernetwork.social.ui.connection.ConnectionScreen

sealed interface ProfileOverlayState {
    data object Empty : ProfileOverlayState

    data class Photo(
        val id: String,
        val position: Int
    ) : ProfileOverlayState

    data class Video(
        val id: String,
        val position: Int
    ) : ProfileOverlayState
}

@Composable
fun ProfileOverlay(
    overlay: MutableState<ProfileOverlayState>,
    userId: String,
    title: String?,
    limit: Int,
    component: Profile.Component,
    viewModelStore: ViewModelState,
    connectionController: ConnectionController,
    content: @Composable () -> Unit
) {
    val updatedContent by rememberUpdatedState(content)
    val key = remember { System.currentTimeMillis().toString() }
    val visible = remember(overlay.value) { mutableStateOf(overlay.value !is ProfileOverlayState.Empty) }
    val context = LocalContext.current
    val cfg     = LocalConfiguration.current

    updatedContent()
    DesignDialogSheet(
        key,
        visible,
        onAnimationComplete = {
            if (!it) {
                component.videoInteractor().restore()
                overlay.value = ProfileOverlayState.Empty
            }
        }
    ) {
        DesignOverlayPage(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .background(MaterialTheme.colorScheme.background)
        ) {
            val controller = rememberNavController()
            ProfileNavigation(
                userId = userId,
                title = title,
                limit = limit,
                startDestination = "overlay",
                controller = controller,
                component = component,
                viewModelStore = viewModelStore,
            ) { controller ->
                when (overlay.value) {
                    is ProfileOverlayState.Photo -> {
                        val state = (overlay.value as ProfileOverlayState.Photo)
                        PhotoOverlay(
                            author = userId,
                            limit = limit,
                            position = state.position,
                            provider = component,
                            viewModelStore.get(userId)
                        )
                    }
                    is ProfileOverlayState.Video -> {
                        val state = (overlay.value as ProfileOverlayState.Video)

                        val videoComponent = remember {
                            component.builder(Video.Builder::class.java)
                                .build(context)
                        }
                        val videoVM = viewModel(
                            modelClass = VideoViewModel::class.java,
                            viewModelStoreOwner = viewModelStore.get(userId),
                            factory = videoComponent.viewModelFactory()
                        )

                        val thumbnail = videoVM.thumbnail.collectAsStateWithLifecycle().value

                        EngagementScreen(
                            postLimit = limit,
                            refresh = remember { mutableStateOf(true) },
                            onMentionClick = { controller.navigateToUsernameSearch(it) },
                            onHashtagClick = { controller.navigateToTagSearch(it) },
                            onAuthorClick = { controller.navigateIfNecessary("profile/$it") },
                            provider = videoComponent,
                            viewModelStoreOwner = viewModelStore.get(userId)
                        ) { engagementEvent ->

                            ModerationScreen(
                                provider = videoComponent,
                                viewModelStoreOwner = viewModelStore.get(userId)
                            ) { moderationEvent ->
                                val connections by connectionController.observe()
                                    .collectAsStateWithLifecycle()
                                val connectionLambda:
                                        @Composable RowScope.(Triple<String, Boolean, Boolean>) -> Unit =
                                    { triple ->
                                        ConnectionScreen(
                                            isFollowing = connections
                                                .getOrDefault(triple.first, triple.third),
                                            isFollowed = triple.second,
                                            onClick = { follow ->
                                                connectionController.invoke(triple.first, !follow)
                                            }
                                        )
                                    }
                                VideoOverlay(
                                    author = userId,
                                    limit = limit,
                                    position = state.position,
                                    provider = component,
                                    viewModel = videoVM,
                                    enabled = visible.value,
                                    connection = connectionLambda,
                                    engagementEvent = engagementEvent,
                                    moderationEvent = moderationEvent,
                                    onLoadBitmap = { thumbnail[it] },
                                    onLoad = { url, ratio ->
                                        videoVM.thumbnail(
                                            url,
                                            UiMimeType.Video,
                                            cfg.screenWidthDp,
                                            ratio
                                        )
                                    }
                                )
                            }
                        }
                    }
                    else -> {}
                }
            }
        }
    }
}
