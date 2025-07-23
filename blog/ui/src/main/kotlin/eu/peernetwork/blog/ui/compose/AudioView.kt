package eu.peernetwork.blog.ui.compose

import android.media.MediaPlayer
import android.net.Uri
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import eu.peernetwork.blog.ui.model.UiAuthor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.File
import java.net.URL
import eu.peernetwork.media.core.R

@Composable
fun AudioView(
    author: UiAuthor,
    description: String,
    audioUrl: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    onAuthorClick: () -> Unit = {},
    contentPadding: PaddingValues = PaddingValues(
        top = 16.dp, start = 16.dp, end = 16.dp, bottom = 8.dp
    ),
    actions: @Composable RowScope.() -> Unit = {},
    engagements: @Composable RowScope.() -> Unit = {},
    moderation: @Composable RowScope.() -> Unit = {},
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current
    var localAudioUri by remember { mutableStateOf<Uri?>(null) }
    val updatedContent by rememberUpdatedState(content)

    LaunchedEffect(audioUrl) {
        val cachedFile = withContext(Dispatchers.IO) {
            val cacheDir = context.cacheDir
            val fileName = audioUrl.substringAfterLast("/").ifBlank { "audio_temp_file" }
            val file = File(cacheDir, fileName)
            if (!file.exists()) {
                try {
                    URL(audioUrl).openStream().use { input ->
                        file.outputStream().use { output -> input.copyTo(output) }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    null
                }
            }
            file
        }
        localAudioUri = cachedFile?.let { Uri.fromFile(it) }
    }

    PostScaffold(
        modifier = modifier,
        header = {
            Row {
                AuthorView(
                    author = author,
                    description = description,
                    modifier = Modifier.weight(1f),
                    onClick = onAuthorClick
                )
                actions()
            }
        },
        toolbar = {},
        background = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(24.dp))
                    .clickable(role = Role.Button, onClick = onClick)
            )
        },
        contentPadding = contentPadding,
        footer = {
            Row(modifier = Modifier.padding(top = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                engagements()
                Spacer(Modifier.weight(1f))
                moderation()
            }
        }
    ) {
        Column(Modifier.fillMaxWidth()) {
            updatedContent()

            Spacer(modifier = Modifier.height(4.dp))

            if (localAudioUri != null) {
                AudioPlayer(audioUri = localAudioUri!!)
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .padding(horizontal = 24.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    repeat(8) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(Color.Gray, shape = RoundedCornerShape(50))
                        )
                    }
                }
            }
        }

    }
}

@Composable
fun AudioPlayer(audioUri: Uri) {
    val context = LocalContext.current
    val primaryColor = MaterialTheme.colorScheme.primary

    var isPlaying by remember { mutableStateOf(false) }
    var progress by remember { mutableStateOf(0f) }
    var currentTime by remember { mutableStateOf(0) }
    var totalDuration by remember { mutableStateOf(0) }

    val mediaPlayer = remember { MediaPlayer() }

    DisposableEffect(audioUri) {
        mediaPlayer.setDataSource(context, audioUri)
        mediaPlayer.prepare()
        totalDuration = mediaPlayer.duration
        onDispose {
            mediaPlayer.stop()
            mediaPlayer.release()
        }
    }

    LaunchedEffect(isPlaying) {
        while (isPlaying && mediaPlayer.isPlaying) {
            currentTime = mediaPlayer.currentPosition
            progress = currentTime.toFloat() / totalDuration.toFloat()
            delay(100L)
        }
        if (!mediaPlayer.isPlaying) isPlaying = false
    }

    fun formatTime(milliseconds: Int): String {
        val totalSeconds = milliseconds / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return "%d:%02d".format(minutes, seconds)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .border(width = 1.dp, color = Color.Gray, shape = RoundedCornerShape(4.dp))
            .padding(horizontal = 8.dp, vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize()
        ) {
            IconButton(
                onClick = {
                    if (mediaPlayer.isPlaying) {
                        mediaPlayer.pause()
                        isPlaying = false
                    } else {
                        mediaPlayer.start()
                        isPlaying = true
                    }
                },
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    painter = painterResource(
                        id = if (isPlaying) R.drawable.ic_pause else R.drawable.ic_play
                    ),
                    contentDescription = if (isPlaying) "Pause" else "Play",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Canvas(
                modifier = Modifier
                    .weight(1f)
                    .height(20.dp)
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragStart = { offset ->
                                val newProgress = (offset.x / size.width).coerceIn(0f, 1f)
                                progress = newProgress
                                val newTime = (mediaPlayer.duration * newProgress).toInt()
                                mediaPlayer.seekTo(newTime)
                                currentTime = newTime
                            },
                            onDrag = { change, _ ->
                                change.consume()
                                val newX = change.position.x.coerceIn(0f, size.width.toFloat())
                                val newProgress = (newX / size.width).coerceIn(0f, 1f)
                                progress = newProgress
                                val newTime = (mediaPlayer.duration * newProgress).toInt()
                                mediaPlayer.seekTo(newTime)
                                currentTime = newTime
                            }
                        )
                    }
            ) {
                val lineHeight = size.height / 2
                val progressX = progress * size.width

                drawLine(
                    color = Color.LightGray,
                    start = Offset(0f, lineHeight),
                    end = Offset(size.width, lineHeight),
                    strokeWidth = 4f
                )
                drawLine(
                    color = primaryColor,
                    start = Offset(0f, lineHeight),
                    end = Offset(progressX, lineHeight),
                    strokeWidth = 6f
                )
                drawCircle(
                    color = primaryColor,
                    radius = 6.dp.toPx(),
                    center = Offset(progressX, lineHeight)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "${formatTime(currentTime)} / ${formatTime(totalDuration)}",
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray,
                modifier = Modifier.width(80.dp)
            )
        }
    }
}
