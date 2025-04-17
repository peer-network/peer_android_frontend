package eu.peernetwork.media.ui.selector.photo

import android.Manifest
import android.content.Context
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import eu.peernetwork.core.ui.design.compose.DesignButton
import eu.peernetwork.media.ui.usecase.PermissionUsecase

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PhotoCreate(
    attachments: MutableState<List<Uri>>,
    onPhotosSelected: (List<Uri>) -> Unit
) {
    val context = LocalContext.current
    var imageUris by remember { mutableStateOf(emptyList<Uri>()) }
    val usecase = remember { PermissionUsecase(context) }
    val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        listOf(Manifest.permission.READ_MEDIA_IMAGES)
    } else {
        listOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    }
    val permissionsState = rememberMultiplePermissionsState(permissions = permissions)

    LaunchedEffect(permissionsState.allPermissionsGranted) {
        if (permissionsState.allPermissionsGranted) {
            imageUris = loadGalleryImages(context)
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (permissionsState.allPermissionsGranted && imageUris.isNotEmpty()) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(4),
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(imageUris.size) { index ->
                        val uri = imageUris[index]
                        val isSelected = attachments.value.contains(uri)
                        Box(
                            modifier = Modifier
                                .aspectRatio(1f)
                                .clickable {
                                    attachments.value = if (isSelected) {
                                        attachments.value - uri
                                    } else {
                                        attachments.value + uri
                                    }
                                    onPhotosSelected(attachments.value)
                                }
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(uri),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .border(
                                        width = 2.dp,
                                        color = if (isSelected) Color.Gray else Color.Transparent
                                    )
                            )

                            if (isSelected) {
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.BottomEnd)
                                        .background(
                                            color = Color.Transparent,
                                            shape = CircleShape
                                        )
                                        .size(24.dp)
                                        .padding(2.dp)
                                        .border(
                                            width = 1.dp,
                                            color = Color.White,
                                            shape = CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "${attachments.value.indexOf(uri) + 1}",
                                        color = Color.White,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
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
                        text = "Photo Access Required",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "To display your photos, please grant access to your media library",
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
    }
}

fun loadGalleryImages(context: Context): List<Uri> {
    val images = mutableListOf<Uri>()
    val projection = arrayOf(MediaStore.Images.Media._ID)
    val uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
    val cursor = context.contentResolver.query(uri, projection, null, null, MediaStore.Images.Media.DATE_ADDED + " DESC")

    cursor?.use {
        val columnIndex = it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
        while (it.moveToNext()) {
            val imageUri = Uri.withAppendedPath(uri, it.getLong(columnIndex).toString())
            images.add(imageUri)
        }
    }
    return images
}