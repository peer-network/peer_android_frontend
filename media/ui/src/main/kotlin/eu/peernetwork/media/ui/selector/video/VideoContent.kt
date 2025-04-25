package eu.peernetwork.media.ui.selector.video

import android.net.Uri
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.design.compose.DesignBottomSheet
import eu.peernetwork.core.ui.design.compose.DesignOption
import eu.peernetwork.core.ui.design.compose.DesignOptionPosition
import eu.peernetwork.core.ui.design.compose.DesignOutlinedButton
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.media.ui.compose.VideoWidget

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoContent(
    username: State<String>,
    userId: State<String>,
    timeStamp: State<String>,
    descriptionText: State<String>,
    isFullscreen: MutableState<Boolean>,
    video: Uri,
    onVideoClick: () -> Unit = {},
    avatar: @Composable (() -> Unit),
    isPlaying: Boolean,
    modifier: Modifier = Modifier
) {
    var isPlaying by remember { mutableStateOf(true) }
    val playerRef = remember { mutableStateOf<ExoPlayer?>(null) }
    var progress by remember { mutableFloatStateOf(0f) }
    var totalDuration by remember { mutableLongStateOf(0L) }
    var isSeeking by remember { mutableStateOf(false) }
    val showDescriptionSheet = remember { mutableStateOf(false) }
    val lineCount = remember { mutableIntStateOf(0) }
    var isFullscreen by remember { mutableStateOf(false) }
    LaunchedEffect(playerRef.value) {
        playerRef.value?.addListener(object : Player.Listener {
            override fun onEvents(player: Player, events: Player.Events) {
                super.onEvents(player, events)
                totalDuration = player.duration.coerceAtLeast(0L)
                progress = if (totalDuration > 0) {
                    player.currentPosition.toFloat() / totalDuration.toFloat()
                } else 0f
            }
        })
    }
    Box(modifier = modifier.fillMaxSize()) {
        VideoWidget(
            videoUri = video,
            modifier = Modifier
                .fillMaxSize()
                .clickable { isPlaying = !isPlaying; onVideoClick() },
            isPlaying = isPlaying,
            playerRef = playerRef
        )
        if (!isPlaying) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable { isPlaying = !isPlaying },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Play",
                    tint = Color.White,
                    modifier = Modifier.size(48.dp)
                )
            }
        }
        if (!isFullscreen) {
            Column(modifier = Modifier.fillMaxSize()) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Box(modifier = Modifier.clickable(role = Role.Button) {}) {
                        avatar()
                    }
                    Column(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(start = 70.dp)
                    ) {
                        Row(modifier = Modifier.padding(top = 8.dp)) {
                            Text(
                                text = username.value,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.width(15.dp))
                            Text(
                                text = userId.value,
                                fontSize = 10.sp,
                                color = Color.DarkGray
                            )
                        }
                        Text(
                            text = timeStamp.value,
                            fontSize = 10.sp,
                            color = Color.DarkGray,
                            modifier = Modifier.padding()
                        )
                    }
                    DesignOutlinedButton(
                        onClick = {},
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                    ) {
                        Text(
                            text = "Follow",
                            fontSize = 12.sp,
                            color = Color.DarkGray,
                            modifier = Modifier.padding()
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.ic_like),
                            contentDescription = "Icon",
                            modifier = Modifier
                                .size(20.dp)
                                .background(Color.Transparent)
                        )
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 8.dp, bottom = 8.dp)
                    .wrapContentSize()
            ) {
                DesignOption(
                    text = "2",
                    modifier = Modifier.wrapContentSize(),
                    painter = painterResource(id = R.drawable.ic_like),
                    contentDescription = "Like",
                    position = DesignOptionPosition.BOTTOM,
                    tint = Color.White,
                    onClick = {}
                )
                Spacer(modifier = Modifier.height(8.dp))
                DesignOption(
                    text = "1",
                    modifier = Modifier.wrapContentSize(),
                    painter = painterResource(id = R.drawable.ic_dislike),
                    contentDescription = "Dislike",
                    position = DesignOptionPosition.BOTTOM,
                    tint = Color.White,
                    onClick = {}
                )
                Spacer(modifier = Modifier.height(16.dp))
                DesignOption(
                    text = "3",
                    modifier = Modifier.wrapContentSize(),
                    painter = painterResource(id = R.drawable.ic_chat_outline),
                    contentDescription = "Comments",
                    position = DesignOptionPosition.BOTTOM,
                    tint = Color.White,
                    onClick = {}
                )
                Spacer(modifier = Modifier.height(16.dp))
                DesignOption(
                    text = "86",
                    modifier = Modifier.wrapContentSize(),
                    painter = painterResource(id = R.drawable.ic_profile_outline),
                    contentDescription = "Profile",
                    position = DesignOptionPosition.BOTTOM,
                    tint = Color.Gray,
                    onClick = {}
                )
                Spacer(modifier = Modifier.height(16.dp))
                DesignOption(
                    text = "",
                    modifier = Modifier.wrapContentSize(),
                    painter = painterResource(id = R.drawable.ic_wallet_outline),
                    contentDescription = "Wallet",
                    position = DesignOptionPosition.BOTTOM,
                    tint = Color.White,
                    onClick = {}
                )
            }
        }
        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 8.dp, bottom = 8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clickable { isFullscreen = !isFullscreen },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(
                        id = if (isFullscreen) R.drawable.ic_edit
                        else R.drawable.ic_add
                    ),
                    contentDescription = if (isFullscreen) "Exit fullscreen" else "Enter fullscreen",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(4.dp))

            if (!isFullscreen && !showDescriptionSheet.value) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 30.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .clickable { showDescriptionSheet.value = true }
                            .background(Color.Black.copy(alpha = 0.4f))
                            .padding(8.dp)
                    ) {
                        val displayText = if (lineCount.intValue > 2) {
                            val layoutResult = remember(descriptionText.value) {
                                mutableStateOf<TextLayoutResult?>(null)
                            }

                            Box {
                                Text(
                                    text = descriptionText.value,
                                    color = Color.Transparent,
                                    maxLines = 2,
                                    onTextLayout = { textLayoutResult ->
                                        layoutResult.value = textLayoutResult
                                        lineCount.intValue = textLayoutResult.lineCount
                                    }
                                )

                                if (layoutResult.value != null) {
                                    val truncatedText =
                                        remember(layoutResult.value, descriptionText.value) {
                                            if (lineCount.intValue > 2) {
                                                val lastVisibleChar =
                                                    layoutResult.value!!.getLineEnd(1)
                                                descriptionText.value.substring(
                                                    0,
                                                    lastVisibleChar - 3
                                                ) + "..."
                                            } else {
                                                descriptionText.value
                                            }
                                        }

                                    Text(
                                        text = truncatedText,
                                        color = Color.White,
                                        maxLines = 2,
                                        overflow = TextOverflow.Visible
                                    )
                                }
                            }
                        } else {
                            Text(
                                text = descriptionText.value,
                                color = Color.White,
                                maxLines = 2,
                                onTextLayout = { textLayoutResult ->
                                    lineCount.intValue = textLayoutResult.lineCount
                                }
                            )
                        }
                    }
                }
            }

        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(if (isSeeking) 4.dp else 2.dp)
                .align(Alignment.BottomCenter)
                .background(Color.White.copy(alpha = 0.3f))
                .animateContentSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress)
                    .height(if (isSeeking) 4.dp else 2.dp)
                    .background(if (isSeeking) Color.White else Color.Gray)
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
                .align(Alignment.BottomCenter)
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragStart = { isSeeking = true },
                        onDragEnd = { isSeeking = false },
                        onHorizontalDrag = { change, _ ->
                            val newProgress = (change.position.x / size.width).coerceIn(0f, 1f)
                            progress = newProgress
//                            playerRef.value?.seekTo((totalDuration * newProgress).toLong())
                        }
                    )
                }
                .background(Color.Transparent)
        )
    }
    if (showDescriptionSheet.value) {
        DesignBottomSheet(
            tag = "videoBottomSheet",
            showSheet = showDescriptionSheet,
            sheetPeekHeight = 300.dp,
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        "Description",
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = descriptionText.value,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            },
        )
    }
}

@Preview
@Composable
fun PreviewVideoContent() {
    PeerTheme {
        var sampleUri = remember { Uri.parse("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4") }
        VideoContent(
            username = remember { mutableStateOf("Sandro") },
            userId = remember { mutableStateOf("#030604") },
            timeStamp = remember { mutableStateOf("2 Hours Ago") },
            video = sampleUri,
            avatar = {},
            descriptionText = remember { mutableStateOf("Rimtariro Ramtariro") },
            isFullscreen = remember { mutableStateOf(false) },
            isPlaying =(true),

        )
    }
}