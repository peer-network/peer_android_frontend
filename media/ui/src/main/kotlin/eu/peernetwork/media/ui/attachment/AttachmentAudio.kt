package eu.peernetwork.media.ui.attachment

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import eu.peernetwork.media.core.model.UiFile

@Composable
fun AttachmentAudio(
    files: List<UiFile>,
    onRemove: (Int) -> Unit,
    onAttach: () -> Unit,
    onSelectCover: (Uri) -> Unit,
    modifier: Modifier = Modifier
) {
    if (files.isEmpty()) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .padding(16.dp)
                .aspectRatio(1f)
                .clip(RoundedCornerShape(24.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .clickable(onClick = onAttach)
        ) {
            Text(
                text = "Upload Audio",
                color = MaterialTheme.colorScheme.tertiary
            )
        }
    } else {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp, horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            files.forEachIndexed { index, file ->
                val coverForFile = file.cover

                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(24.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    if (coverForFile != null) {
                        Image(
                            painter = rememberAsyncImagePainter(coverForFile),
                            contentDescription = "Cover Image",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(8.dp))
                        )
                    } else {
                        Icon(
                            painter = painterResource(id = eu.peernetwork.core.ui.R.drawable.ic_music),
                            contentDescription = "Audio Icon",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(48.dp)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(8.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.8f))
                            .clickable { onSelectCover(file.uri) }
                            .padding(6.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = eu.peernetwork.core.ui.R.drawable.ic_edit),
                            contentDescription = "Select Cover Image",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(14.dp)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(8.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.background)
                            .clickable { onRemove(index) }
                            .padding(6.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = eu.peernetwork.core.ui.R.drawable.ic_cancel),
                            contentDescription = "Remove Audio",
                            tint = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                }
            }
        }
    }
}
