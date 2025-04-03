package eu.peernetwork.app.ui.creator

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.core.ui.R

enum class CreatorTab {
    PHOTO, VIDEO, TEXT, MUSIC
}

@Composable
fun CreatorScreen() {
    var selectedTab by remember { mutableStateOf(CreatorTab.PHOTO) }
    var selectedImages by remember { mutableStateOf(listOf<Uri>()) }
    var selectedVideo by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    var latestImageUri by remember { mutableStateOf<Uri?>(null) }

    LaunchedEffect(selectedTab) {
        when (selectedTab) {
            CreatorTab.PHOTO -> {
                selectedVideo = null
                latestImageUri = loadLatestGalleryImage(context)
            }
            CreatorTab.VIDEO -> selectedImages = emptyList()
            else -> {
                selectedImages = emptyList()
                selectedVideo = null
            }
        }
    }

    if (selectedTab == CreatorTab.TEXT) {
        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(MaterialTheme.colorScheme.surface),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CreatorTab.values().forEach { tab ->
                    TabIcon(
                        tab = tab,
                        isSelected = selectedTab == tab,
                        onClick = { selectedTab = tab }
                    )
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                TextCreate()
            }
        }
    } else {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                when {
                    selectedImages.isNotEmpty() -> {
                        PhotoEdit(
                            imageUris = selectedImages,
                            onClearImages = { selectedImages = emptyList() }
                        )
                    }
                    selectedVideo != null -> {
                        VideoEdit(
                            videoUri = selectedVideo,
                            onClearVideo = { selectedVideo = null }
                        )
                    }
                    selectedTab == CreatorTab.PHOTO -> {
                        if (latestImageUri != null) {
                            Image(
                                painter = rememberAsyncImagePainter(latestImageUri),
                                contentDescription = "Latest photo",
                                modifier = Modifier
                                    .fillMaxSize(),
                                contentScale = ContentScale.Fit
                            )
                        } else {
                            Text("No photos found")
                        }
                    }
                    selectedTab == CreatorTab.VIDEO -> Text("Select video to edit")
                    selectedTab == CreatorTab.MUSIC -> MusicEdit()
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(MaterialTheme.colorScheme.surface),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CreatorTab.values().forEach { tab ->
                    TabIcon(
                        tab = tab,
                        isSelected = selectedTab == tab,
                        onClick = { selectedTab = tab }
                    )
                }
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                when (selectedTab) {
                    CreatorTab.PHOTO -> PhotoCreate { uris ->
                        selectedImages = uris
                    }
                    CreatorTab.VIDEO -> VideoCreate { uri ->
                        selectedVideo = uri
                    }
                    CreatorTab.MUSIC -> MusicCreate()
                    else -> {}
                }
            }
        }
    }
}

@Composable
fun TabIcon(
    tab: CreatorTab,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val (iconResId, labelText) = when (tab) {
        CreatorTab.PHOTO -> Pair(eu.peernetwork.social.ui.R.drawable.ic_photo, "Photo")
        CreatorTab.VIDEO -> Pair(eu.peernetwork.social.ui.R.drawable.ic_video, "Video")
        CreatorTab.TEXT -> Pair(R.drawable.ic_chat_outline, "Text")
        CreatorTab.MUSIC -> Pair(eu.peernetwork.social.ui.R.drawable.ic_music, "Music")
    }

    Column(
        modifier = modifier
            .fillMaxHeight()
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = iconResId),
            contentDescription = labelText,
            tint = if (isSelected) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = labelText,
            style = MaterialTheme.typography.labelSmall,
            color = if (isSelected) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}

fun loadLatestGalleryImage(context: Context): Uri? {
    val projection = arrayOf(MediaStore.Images.Media._ID)
    val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    val cursor = context.contentResolver.query(
        uri,
        projection,
        null,
        null,
        "${MediaStore.Images.Media.DATE_ADDED} DESC LIMIT 1"
    )

    return cursor?.use {
        if (it.moveToFirst()) {
            val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            Uri.withAppendedPath(uri, it.getLong(columnIndex).toString())
        } else {
            null
        }
    }
}

@Preview
@Composable
fun PreviewCreatorScreen() {
    PeerTheme {
        CreatorScreen()
    }
}