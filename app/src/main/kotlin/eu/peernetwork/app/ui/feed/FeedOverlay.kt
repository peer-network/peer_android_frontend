package eu.peernetwork.app.ui.feed

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import eu.peernetwork.blog.domain.model.Filter.Criteria
import eu.peernetwork.blog.ui.engagement.EngagementScreen
import eu.peernetwork.blog.ui.moderation.ModerationScreen
import eu.peernetwork.blog.ui.timeline.photo.PhotoOverlay
import eu.peernetwork.blog.ui.timeline.video.Video
import eu.peernetwork.blog.ui.timeline.video.VideoOverlay
import eu.peernetwork.core.ui.design.compose.DesignDialogSheet
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.design.compose.DesignOverlayPage
import eu.peernetwork.core.ui.extension.navigateIfNecessary
import eu.peernetwork.core.ui.model.ViewModelState
import eu.peernetwork.social.ui.connection.ConnectionController
import eu.peernetwork.social.ui.connection.ConnectionScreen

sealed interface FeedOverlayState {
    data object Empty : FeedOverlayState

    data class Photo(
        val id: String,
        val position: Int
    ) : FeedOverlayState

    data class Video(
        val id: String,
        val position: Int
    ) : FeedOverlayState
}

@Composable
fun FeedOverlay(
    overlay: MutableState<FeedOverlayState>,
    userId: String,
    postLimit: Int,
    criteria: Criteria? = null,
    component: Feed.Component,
    viewModelStore: ViewModelState,
    connectionController: ConnectionController,
    content: @Composable () -> Unit
) {
    val key = remember { System.currentTimeMillis().toString() }
    val updatedContent by rememberUpdatedState(content)
    val viewModelStoreOwner = viewModelStore.get(criteria?.toString() ?: userId)
    val visible = remember(overlay.value) { mutableStateOf(overlay.value !is FeedOverlayState.Empty) }
    val context = LocalContext.current
    updatedContent()
    DesignDialogSheet(
        key,
        visible,
        onAnimationComplete = {
            if (!it) {
                component.videoInteractor().restore()
                overlay.value = FeedOverlayState.Empty
            }
        }
    ) {
        DesignOverlayPage(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            val controller = rememberNavController()
            FeedNavigation(
                userId = userId,
                startDestination = "overlay",
                postLimit = postLimit,
                controller = controller,
                component = component,
                viewModelStore = viewModelStore,
            ) {
                when (overlay.value) {
                    is FeedOverlayState.Photo -> {
                        val state = (overlay.value as FeedOverlayState.Photo)
                        PhotoOverlay(
                            postLimit,
                            state.position,
                            component,
                            viewModelStoreOwner,
                            onMentionClick = { controller.navigateToUsernameSearch(it) },
                            onHashtagClick = { controller.navigateToTagSearch(it) },
                            onAuthorClick = { controller.navigateIfNecessary("profile/$it") },
                        )
                    }
                    is FeedOverlayState.Video -> {
                        val state = (overlay.value as FeedOverlayState.Video)

                        val videoComponent = remember {
                            component.builder(Video.Builder::class.java)
                                .build(context)
                        }
                        EngagementScreen(
                            postLimit           = postLimit,
                            refresh             = remember { mutableStateOf(true) },
                            onMentionClick      = { controller.navigateToUsernameSearch(it) },
                            onHashtagClick      = { controller.navigateToTagSearch(it) },
                            onAuthorClick       = { controller.navigateIfNecessary("profile/$it") },
                            provider            = videoComponent,
                            viewModelStoreOwner = viewModelStoreOwner
                        ) { engagementEvent ->

                            ModerationScreen(
                                provider = videoComponent,
                                viewModelStoreOwner = viewModelStoreOwner
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
                                    id                   = userId,
                                    limit                = postLimit,
                                    position             = state.position,
                                    enabled              = visible.value,
                                    provider             = component,
                                    viewModelStoreOwner  = viewModelStoreOwner,
                                    onAuthorClick        = { controller.navigateIfNecessary("profile/$it") },
                                    onMentionClick       = { controller.navigateToUsernameSearch(it) },
                                    onHashtagClick       = { controller.navigateToTagSearch(it) },
                                    connection           = connectionLambda,
                                    engagementEvent      = engagementEvent,
                                    moderationEvent      = moderationEvent
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
