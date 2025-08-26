package eu.peernetwork.blog.ui.compose

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import eu.peernetwork.blog.ui.model.UiAction
import eu.peernetwork.blog.ui.model.UiAuthor
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.design.compose.DesignTextButton
import eu.peernetwork.core.ui.theme.PeerTheme
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun BoxScope.ConfirmedLikeHeartOverlay(
    isLiked: Boolean,
    modifier: Modifier = Modifier,
    size: Dp = 160.dp,
    rawRes: Int = R.raw.like
) {
    val (play, setPlay) = remember { mutableStateOf(false) }
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(rawRes))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        isPlaying = play,
        iterations = 1,
        speed = 1f
    )
    var prev by remember { mutableStateOf(isLiked) }
    LaunchedEffect(isLiked) {
        if (!prev && isLiked) {
            setPlay(false)
            setPlay(true)
        }
        prev = isLiked
    }
    LaunchedEffect(progress) {
        if (progress >= 1f) setPlay(false)
    }

    AnimatedVisibility(
        visible = play && progress < 1f,
        enter = fadeIn(animationSpec = tween(160)),
        exit = fadeOut(animationSpec = tween(220)),
        modifier = modifier
    ) {
        LottieAnimation(
            composition = composition,
            progress = { progress },
            modifier = Modifier.size(size)
        )
    }
}

@Composable
fun MediaView(
    author: UiAuthor,
    description: String,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    isLiked: Boolean = false,
    onClick: () -> Unit = {},
    onDoubleClick: () -> Unit = {},
    onAuthorClick: () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    engagements: @Composable RowScope.() -> Unit = {},
    moderation: @Composable RowScope.() -> Unit = {},
    caption: @Composable () -> Unit = {},
    content: @Composable () -> Unit,
) {
    val updatedAction by rememberUpdatedState(actions)
    val updatedContent by rememberUpdatedState(content)
    val updatedCaption by rememberUpdatedState(caption)
    val updatedEngagements by rememberUpdatedState(engagements)
    val updatedModeration by rememberUpdatedState(moderation)
    val click by rememberUpdatedState(onClick)
    val dblClick by rememberUpdatedState(onDoubleClick)

    val scope = rememberCoroutineScope()
    val (singleTapJob, setSingleTapJob) = remember { mutableStateOf<Job?>(null) }

    PostScaffold(
        modifier = modifier,
        header = {},
        toolbar = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 4.dp)
                    .padding(horizontal = 24.dp)
            ) {
                updatedEngagements()
                Spacer(modifier = Modifier.weight(1f))
                updatedModeration()
            }
            Box(modifier = Modifier.padding(horizontal = 24.dp)) { updatedCaption() }
        },
        background = {},
        contentPadding = contentPadding,
        footer = { }
    ) {
        Box(
            modifier = Modifier
                .semantics { role = Role.Button }
                .pointerInput(Unit) {
                    detectTapGestures(
                        onTap = {
                            val job = scope.launch {
                                delay(120)
                                click()
                            }
                            setSingleTapJob(job)
                        },
                        onDoubleTap = {
                            singleTapJob?.cancel()
                            dblClick()
                        }
            ) }
        ) {
            updatedContent()
            ConfirmedLikeHeartOverlay(
                isLiked = isLiked,
                modifier = Modifier.align(Alignment.Center),
                size = 160.dp
            )

            Image(
                painter = painterResource(R.drawable.overlay_gradient),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth()
                    .height(120.dp)
                    .rotate(180f),
                contentScale = ContentScale.FillWidth
            )
            Row(modifier = Modifier.padding(
                vertical = 16.dp,
                horizontal = 24.dp
            )) {
                AuthorView(
                    author,
                    description,
                    onClick = onAuthorClick,
                    modifier = Modifier.weight(1f),
                    descriptionColor = MaterialTheme.colorScheme.onBackground
                )
                updatedAction()
            }
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewMediaPreview() {
    PeerTheme {
        MediaView(
            modifier = Modifier.padding(bottom = 24.dp),
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
            },
            caption = {
                TextView(
                    "JohnDoe",
                    buildAnnotatedString { append("John Doe") },
                    buildAnnotatedString { append("Description...") }
                )
            }
        ) {
            Box(modifier = Modifier.fillMaxWidth()
                .height(200.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant)
            )
        }
    }
}