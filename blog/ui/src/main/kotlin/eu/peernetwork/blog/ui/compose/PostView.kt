package eu.peernetwork.blog.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.blog.ui.model.UiAction
import eu.peernetwork.blog.ui.model.UiAuthor
import eu.peernetwork.core.ui.design.compose.DesignTextButton
import eu.peernetwork.core.ui.theme.PeerTheme
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import eu.peernetwork.core.ui.R
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut

@Composable
fun TextPreview(
    author: UiAuthor,
    description: String,
    modifier: Modifier = Modifier,
    isLiked: Boolean = false,
    onClick: () -> Unit = {},
    onDoubleClick: () -> Unit = {},
    onAuthorClick: () -> Unit = {},
    contentPadding: PaddingValues = PaddingValues(
        top = 16.dp,
        start = 16.dp,
        end = 16.dp,
        bottom = 8.dp
    ),
    actions: @Composable RowScope.() -> Unit = {},
    engagements: @Composable RowScope.() -> Unit = {},
    moderation: @Composable RowScope.() -> Unit = {},
    content: @Composable () -> Unit,
) {
    val updatedAction by rememberUpdatedState(actions)
    val updatedContent by rememberUpdatedState(content)
    val updatedEngagements by rememberUpdatedState(engagements)
    val updatedModeration by rememberUpdatedState(moderation)
    val tap by rememberUpdatedState(onClick)
    val dblTap by rememberUpdatedState(onDoubleClick)

    val (playHeart, setPlayHeart) = remember { mutableStateOf(false) }
    val composition by rememberLottieComposition(
        LottieCompositionSpec.RawRes(R.raw.like)
    )
    val heartProgress by animateLottieCompositionAsState(
        composition = composition,
        isPlaying = playHeart,
        iterations = 1,
        speed = 1.0f
    )
    val (prevLiked, setPrevLiked) = remember { mutableStateOf(isLiked) }
    LaunchedEffect(isLiked) {
        if (!prevLiked && isLiked) {
            setPlayHeart(false)
            setPlayHeart(true)
        }
        setPrevLiked(isLiked)
    }
    LaunchedEffect(heartProgress) {
        if (heartProgress >= 1f) setPlayHeart(false)
    }

    val scope = rememberCoroutineScope()
    val (singleTapJob, setSingleTapJob) = remember { mutableStateOf<Job?>(null) }

    PostScaffold(
        modifier = modifier,
        header = {
            Row {
                AuthorView(
                    author,
                    description,
                    modifier = Modifier.weight(1f),
                    onClick = onAuthorClick
                )
                updatedAction()
            }
        },
        toolbar = {},
        background = {
            Box(
                modifier = Modifier.fillMaxSize()
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant,
                        RoundedCornerShape(24.dp)
                    )
                    .semantics { role = Role.Button }
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = {
                                val job = scope.launch {
                                    delay(120)
                                    tap()
                                }
                                setSingleTapJob(job)
                            },
                            onDoubleTap = {
                                singleTapJob?.cancel()
                                dblTap()
                            }
                        )
                    }
            ) {
                AnimatedVisibility(
                    visible = playHeart && heartProgress < 1f,
                    enter = fadeIn(animationSpec = tween(160)),
                    exit = fadeOut(animationSpec = tween(220)),
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    LottieAnimation(
                        composition = composition,
                        progress = { heartProgress },
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(140.dp)
                    )
                } } },
        contentPadding = contentPadding,
        footer = {
            Row(
                modifier = Modifier.padding(top = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                updatedEngagements()
                Spacer(modifier = Modifier.weight(1f))
                updatedModeration()
            }
        }
    ) { updatedContent() }
}

@Composable
@Preview
fun PreviewTextPostCard() {
    PeerTheme {
        TextPreview(
            modifier = Modifier.padding(horizontal = 8.dp),
            author = UiAuthor(
                id = "",
                slug = 12034,
                username = "JohnDoe",
                imageUrl = "http://localhost",
                isfollowing = false,
                isfollowed = false
            ),
            description = "Description...",
            isLiked = false,
            engagements = {
                UiAction.ENGAGEMENTS.forEach {
                    DesignTextButton(
                        onClick = {},
                        contentPadding = PaddingValues(2.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                painter = painterResource(id = it.id),
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.tertiary,
                                modifier = Modifier.size(32.dp)
                            )
                            Text("0", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }
        ) {
            Box(modifier = Modifier.fillMaxWidth().height(200.dp))
        }
    }
}
