package eu.peernetwork.media.ui.editor.audio

import android.media.AudioAttributes
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.compose.DesignOutlinedButton
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.util.Locale
import eu.peernetwork.media.ui.R
import kotlin.math.floor

@Composable
fun AudioPreviewScreen(
    source: String,
    onDiscard: () -> Unit,
    onProceed: (Long, Long, Long) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val durationMs = rememberSaveable(source) { mutableStateOf(0L) }
    val isPrepared = remember { mutableStateOf(false) }
    val player = remember(source) { MediaPlayer() }
    val currentPosMs = remember { mutableStateOf(0L) }
    val updating = remember { mutableStateOf(false) }

    LaunchedEffect(source) {
        isPrepared.value = false
        durationMs.value = 0L
        currentPosMs.value = 0L
        runCatching { player.reset() }

        runCatching {
            player.setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )

            val looksLikeContent = source.startsWith("content://")
            val fileExists = !looksLikeContent && File(source).exists()

            when {
                looksLikeContent -> player.setDataSource(context, Uri.parse(source))
                fileExists -> player.setDataSource(source)
                else -> {
                    if (source.isNotBlank()) {
                        player.setDataSource(context, Uri.parse(source))
                    } else {
                        isPrepared.value = true
                        return@runCatching
                    }
                }
            }

            player.setOnPreparedListener {
                durationMs.value = it.duration.toLong()
                isPrepared.value = true
                updating.value = true
                it.start()
            }
            player.setOnErrorListener { _, _, _ ->
                isPrepared.value = true
                updating.value = false
                true
            }
            player.prepareAsync()

            scope.launch {
                runCatching {
                    val retriever = MediaMetadataRetriever()
                    if (looksLikeContent || !File(source).exists()) {
                        retriever.setDataSource(context, Uri.parse(source))
                    } else {
                        retriever.setDataSource(source)
                    }
                    retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                        ?.toLongOrNull()
                        ?.takeIf { it > 0 }
                        ?.let { metaDur ->
                            if (durationMs.value <= 0L) durationMs.value = metaDur
                        }
                    retriever.release()
                }
            }
        }.onFailure {
            isPrepared.value = true
            updating.value = false
        }
    }

    LaunchedEffect(isPrepared.value) {
        if (!isPrepared.value) return@LaunchedEffect
        updating.value = true
    }

    LaunchedEffect(updating.value) {
        while (updating.value) {
            if (isPrepared.value && player.isPlaying) currentPosMs.value = player.currentPosition.toLong()
            delay(120)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            updating.value = false
            runCatching { player.stop() }
            runCatching { player.release() }
        }
    }

    val currentReadable = remember(currentPosMs.value) { currentPosMs.value.readable() }
    val endReadable = remember(durationMs.value) { durationMs.value.readable() }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 32.dp)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = visibleName(source),
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                modifier = Modifier.padding(12.dp)
            )
        }
        Spacer(Modifier.height(32.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = 160.dp)
        ) {
            DesignOutlinedButton(
                onClick = {
                    if (isPrepared.value) onProceed(0L, durationMs.value, durationMs.value)
                },
                enabled = isPrepared.value,
                isLoading = !isPrepared.value,
                shape = MaterialTheme.shapes.extraLarge,
                textStyle = MaterialTheme.typography.labelLarge,
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = MaterialTheme.colorScheme.onBackground,
                    contentColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                minHeight = 40.dp,
                border = ButtonDefaults.outlinedButtonBorder(enabled = isPrepared.value)
            ) { Text(stringResource(R.string.continue_label)) }

            Spacer(Modifier.width(12.dp))

            DesignOutlinedButton(
                onClick = onDiscard,
                enabled = true,
                isLoading = false,
                shape = MaterialTheme.shapes.extraLarge,
                textStyle = MaterialTheme.typography.labelLarge,
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onBackground
                ),
                minHeight = 40.dp,
                border = ButtonDefaults.outlinedButtonBorder(enabled = true)
            ) { Text(stringResource(R.string.discard_label)) }

            Spacer(Modifier.weight(1f))

            Text(
                currentReadable,
                style = MaterialTheme.typography.labelLarge.copy(
                    color = MaterialTheme.colorScheme.tertiary
                )
            )
            Spacer(Modifier.width(8.dp))
            Text(
                endReadable,
                style = MaterialTheme.typography.labelLarge.copy(
                    color = MaterialTheme.colorScheme.tertiary
                )
            )
        }
    }
}

private fun visibleName(source: String): String {
    if (source.startsWith("content://")) return "Audio"
    val f = File(source)
    return f.name.takeIf { it.isNotBlank() } ?: "Audio"
}

private fun Long.readable(locale: Locale = Locale.US): String {
    if (this <= 0) return "0.00s"
    val total = this / 1000.0
    val h = floor(total / 3600).toInt()
    val m = floor((total % 3600) / 60).toInt()
    val s = total % 60
    return when {
        h > 0 -> String.format(locale, "%d:%02d:%05.2f", h, m, s)
        m > 0 -> String.format(locale, "%d:%05.2f", m, s)
        else -> String.format(locale, "%.2fs", s)
    }
}