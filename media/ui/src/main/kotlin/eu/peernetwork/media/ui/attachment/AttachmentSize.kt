package eu.peernetwork.media.ui.attachment

import android.content.ContentResolver
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.design.compose.DesignBottomSheet
import eu.peernetwork.core.ui.design.compose.DesignBottomSheetScaffold
import eu.peernetwork.core.ui.design.compose.DesignButton
import eu.peernetwork.core.ui.design.compose.DesignDialog
import eu.peernetwork.media.core.model.UiAttachment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.DecimalFormat

@Composable
fun AttachmentSize(
    attachment: UiAttachment,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    var sizeText by remember { mutableStateOf("…") }
    var exceedsLimit by remember { mutableStateOf(false) }
    val showWarning = remember { mutableStateOf(false) }
    val maxSizeBytes = 500L * 1024L * 1024L // 500 MB
    var wasExceedingLimit by remember { mutableStateOf(false) }
    val normalTextStyle = MaterialTheme.typography.bodyMedium
        .copy(color = MaterialTheme.colorScheme.tertiary)
    val errorTextStyle = MaterialTheme.typography.bodyMedium
        .copy(color = MaterialTheme.colorScheme.error)

    LaunchedEffect(attachment) {
        val totalBytes = withContext(Dispatchers.IO) {
            attachment.files.sumOf { file ->
                file.uri.getFileSize(context.contentResolver)
            }
        }
        val newExceeds = totalBytes > maxSizeBytes
        exceedsLimit = newExceeds
        val current = totalBytes.formatAsSize()
        val limit = maxSizeBytes.formatAsSize()
        sizeText = "$current / $limit"
        if (newExceeds && !wasExceedingLimit) {
            showWarning.value = true
        }
        wasExceedingLimit = newExceeds
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.tertiaryContainer)
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text(
                text = sizeText,
                style = if (exceedsLimit) errorTextStyle else normalTextStyle,
            )
        }
    }

    DesignBottomSheetScaffold(
        state = showWarning,
        onDismiss = { showWarning.value = false }
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colorScheme.surface)
                .padding(24.dp)
                .navigationBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = "Warning",
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .padding(bottom = 12.dp)
                    .size(52.dp)
            )

            Text(
                text = "Oops! That's a bit too big.",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.error
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.padding(8.dp))

            Text(
                text = "Your upload is over ${maxSizeBytes.formatAsSize()}.\nTry compressing the file or picking a smaller one.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.padding(12.dp))

            DesignButton(
                onClick = {showWarning.value = false},
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Okay")
            }
        }
    }
}

private fun Uri.getFileSize(resolver: ContentResolver): Long {
    return try {
        resolver.openAssetFileDescriptor(this, "r")?.use { it.length } ?: 0L
    } catch (e: Exception) {
        0L
    }
}

private fun Long.formatAsSize(): String {
    if (this <= 0) return "0 B"
    val units = arrayOf("B", "KB", "MB", "GB", "TB")
    val digitGroups = (Math.log10(this.toDouble()) / Math.log10(1024.0)).toInt()
    return DecimalFormat("#,##0.#").format(this / Math.pow(1024.0, digitGroups.toDouble())) + " " + units[digitGroups]
}
