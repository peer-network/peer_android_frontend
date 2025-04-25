package eu.peernetwork.media.ui.selector.video

import android.Manifest
import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.media.ThumbnailUtils
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import eu.peernetwork.core.ui.design.compose.DesignBottomSheet
import eu.peernetwork.core.ui.design.compose.DesignButton
import eu.peernetwork.media.ui.usecase.PermissionUsecase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun VideoCreate(
    attachments: MutableState<List<Uri>>,
    onVideoSelected: (Uri?) -> Unit
) {
    val context = LocalContext.current
    var videoUris by remember { mutableStateOf(listOf<Uri>()) }
    val showSelectedVideoState = remember { mutableStateOf(false) }
    val usecase = remember { PermissionUsecase(context) }
    val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        listOf(Manifest.permission.READ_MEDIA_VIDEO)
    } else {
        listOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    }
    val permissionsState = rememberMultiplePermissionsState(permissions = permissions)

    LaunchedEffect(permissionsState.allPermissionsGranted) {
        if (permissionsState.allPermissionsGranted) {
            videoUris = loadGalleryVideos(context)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (permissionsState.allPermissionsGranted && videoUris.isNotEmpty()) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(videoUris.size) { index ->
                        val uri = videoUris[index]
                        var thumbnailBitmap by remember { mutableStateOf<Bitmap?>(null) }
                        var duration by remember { mutableStateOf<String?>(null) }
                        val isSelected = attachments.value.contains(uri)

                        LaunchedEffect(uri) {
                            thumbnailBitmap = withContext(Dispatchers.IO) {
                                getVideoThumbnail(context, uri)
                            }
                            duration = withContext(Dispatchers.IO) {
                                getVideoDuration(context, uri)
                            }
                        }

                        Box(
                            modifier = Modifier
                                .aspectRatio(1f)
                                .clickable {
                                    attachments.value = if (isSelected) {
                                        emptyList()
                                    } else {
                                        listOf(uri)
                                    }
                                    onVideoSelected(if (isSelected) null else uri)
                                }
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .border(
                                        width = 2.dp,
                                        color = if (attachments.value.contains(uri)) Color.Gray else Color.Transparent
                                    )
                            ) {
                                if (thumbnailBitmap != null) {
                                    Image(
                                        bitmap = thumbnailBitmap!!.asImageBitmap(),
                                        contentDescription = "Video thumbnail",
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(Color.Gray),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            Icons.Default.PlayArrow,
                                            contentDescription = "Video",
                                            tint = Color.White,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }

                                if (duration != null) {
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.BottomStart)
                                            .padding(2.dp)
                                    ) {
                                        Text(
                                            text = duration!!,
                                            color = Color.White,
                                            fontSize = 12.sp
                                        )
                                    }
                                }

                                if (isSelected) {
                                    Box(
                                        modifier = Modifier
                                            .align(Alignment.BottomEnd)
                                            .background(
                                                color = Color.Transparent,
                                                shape = CircleShape
                                            )
                                            .size(24.dp)
                                            .padding(2.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            Icons.Default.CheckCircle,
                                            contentDescription = "Selected",
                                            tint = Color.White,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            } else if (!permissionsState.allPermissionsGranted) {
                Spacer(modifier = Modifier.weight(.1f))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(.9f)
                        .padding(horizontal = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = eu.peernetwork.core.ui.R.drawable.ic_error),
                        contentDescription = "Permission required",
                        modifier = Modifier.size(64.dp),
                        alpha = 0.7f
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Video Access Required",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "To display your videos, please grant access to your media library",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    DesignButton(
                        onClick = { usecase() },
                        modifier = Modifier
                            .height(48.dp)
                            .fillMaxWidth(0.7f),
                        shape = RoundedCornerShape(12.dp),
                        content = {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Spacer(modifier = Modifier.weight(1f))
                                Image(
                                    painter = painterResource(id = eu.peernetwork.core.ui.R.drawable.ic_settings),
                                    contentDescription = "Settings",
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = "Open Settings",
                                    style = MaterialTheme.typography.labelLarge
                                )
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    )
                }
            }
        }
        val gradient = Brush.horizontalGradient(
            colors = listOf(
                MaterialTheme.colorScheme.secondary,
                MaterialTheme.colorScheme.primary,
            )
        )

        if (attachments.value.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(brush = gradient)
                        .size(48.dp)
                        .clickable { showSelectedVideoState.value = true },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = attachments.value.size.toString(),
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        if (attachments.value.isNotEmpty()) {
            DesignBottomSheet(
                showSheet = showSelectedVideoState,
                onDismissRequest = {
                    showSelectedVideoState.value = false
                },
                sheetPeekHeight = 600.dp,
                tag = "selected_photos_sheet",
                content = {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.tertiaryContainer)
                            .padding(16.dp)
                    ) {
                        Column {
                            Text(
                                text = "Selected Video: ",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(bottom = 16.dp),
                                color = MaterialTheme.colorScheme.onPrimary
                            )

                            LazyVerticalGrid(
                                columns = GridCells.Fixed(1),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(attachments.value.size) { index ->
                                    val uri = attachments.value[index]
                                    Box(
                                        modifier = Modifier
                                            .aspectRatio(1f)
                                            .clickable {
                                                attachments.value = emptyList()
                                                onVideoSelected(null)
                                                showSelectedVideoState.value = false
                                            }
                                    ) {
                                        var thumbnailBitmap by remember { mutableStateOf<Bitmap?>(null) }

                                        LaunchedEffect(uri) {
                                            thumbnailBitmap = withContext(Dispatchers.IO) {
                                                getVideoThumbnail(context, uri)
                                            }
                                        }

                                        if (thumbnailBitmap != null) {
                                            Image(
                                                bitmap = thumbnailBitmap!!.asImageBitmap(),
                                                contentDescription = "Selected video",
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier.fillMaxSize()
                                            )
                                        } else {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .background(Color.Gray),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    Icons.Default.PlayArrow,
                                                    contentDescription = "Video",
                                                    tint = Color.White,
                                                    modifier = Modifier.size(24.dp)
                                                )
                                            }
                                        }

                                        Box(
                                            modifier = Modifier
                                                .align(Alignment.BottomEnd)
                                                .background(
                                                    color = Color.Transparent,
                                                    shape = CircleShape
                                                )
                                                .size(24.dp)
                                                .padding(2.dp),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(
                                                Icons.Default.CheckCircle,
                                                contentDescription = "Selected",
                                                tint = Color.White,
                                                modifier = Modifier.fillMaxSize()
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            )
        }
    }
}

private fun getVideoThumbnail(context: Context, uri: Uri): Bitmap? {
    return try {
        val filePathColumn = arrayOf(MediaStore.Video.Media.DATA)
        val cursor = context.contentResolver.query(uri, filePathColumn, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val columnIndex = it.getColumnIndex(filePathColumn[0])
                val filePath = it.getString(columnIndex)
                ThumbnailUtils.createVideoThumbnail(
                    filePath,
                    MediaStore.Images.Thumbnails.MINI_KIND
                )
            } else {
                null
            }
        }
    } catch (e: Exception) {
        null
    }
}

private fun getVideoDuration(context: Context, uri: Uri): String {
    val retriever = MediaMetadataRetriever()
    return try {
        retriever.setDataSource(context, uri)
        val durationMs = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLong() ?: 0L
        retriever.release()
        val minutes = (durationMs / 1000) / 60
        val seconds = (durationMs / 1000) % 60
        String.format("%02d:%02d", minutes, seconds)
    } catch (e: Exception) {
        retriever.release()
        "00:00"
    }
}

fun loadGalleryVideos(context: Context): List<Uri> {
    val videos = mutableListOf<Uri>()
    val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
    } else {
        MediaStore.Video.Media.EXTERNAL_CONTENT_URI
    }
    val supportedMimeTypes = arrayOf(
        "video/mp4",
        "video/quicktime",
        "video/x-matroska",
        "video/webm"
    )
    val projection = arrayOf(
        MediaStore.Video.Media._ID,
        MediaStore.Video.Media.DISPLAY_NAME,
        MediaStore.Video.Media.MIME_TYPE
    )
    val selection = "${MediaStore.Video.Media.MIME_TYPE} IN (${supportedMimeTypes.joinToString(",") { "?" }})"
    val selectionArgs = supportedMimeTypes

    context.contentResolver.query(
        collection,
        projection,
        selection,
        selectionArgs,
        "${MediaStore.Video.Media.DATE_ADDED} DESC"
    )?.use { cursor ->
        val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
        val nameColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)

        while (cursor.moveToNext()) {
            val id = cursor.getLong(idColumn)
            val fileName = cursor.getString(nameColumn)

            if (fileName?.let { name ->
                    name.endsWith(".mp4", ignoreCase = true) ||
                            name.endsWith(".mov", ignoreCase = true) ||
                            name.endsWith(".mkv", ignoreCase = true) ||
                            name.endsWith(".webm", ignoreCase = true)
                } == true) {
                val contentUri = ContentUris.withAppendedId(collection, id)
                videos.add(contentUri)
            }
        }
    }
    return videos
}