package eu.peernetwork.media.ui.attachment

import android.Manifest
import android.net.Uri
import android.os.Build
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.media.core.model.UiAttachment
import eu.peernetwork.media.core.model.UiFile
import eu.peernetwork.media.core.model.UiMimeType
import eu.peernetwork.media.ui.selector.photo.PhotoEditor
import eu.peernetwork.media.ui.usecase.PermissionUsecase
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce

@Composable
@OptIn(ExperimentalPermissionsApi::class, FlowPreview::class)
fun AttachmentScreen(
    attachment: MutableState<UiAttachment>,
    onAttach: () -> Unit,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Attachment.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = AttachmentViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    var timestamp by remember { mutableStateOf<Long?>(null) }
    val usecase = remember { PermissionUsecase(context) }
    val counter by remember { derivedStateOf {
        (state as? AttachmentViewModel.State.Success?)?.counter ?: 0
    } }
    val permissionsState = rememberMultiplePermissionsState(
        permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            listOf(Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO)
        } else {
            listOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    )
    val handleOnAttach by rememberUpdatedState(onAttach)
    val thumbnail = viewModel.thumbnail.collectAsStateWithLifecycle().value

    val imageToCrop = remember { mutableStateOf<Uri?>(null) }
    val selectedRatio = remember { mutableStateOf(CropRatio.Square) }
    val shouldLaunchCrop = remember { mutableStateOf(false) }

    Column {
        Crossfade(attachment.value) { target ->
            if (target.files.isEmpty()) {
                AttachmentPlaceholder {
                    if (permissionsState.allPermissionsGranted) {
                        handleOnAttach()
                    } else {
                        timestamp = System.currentTimeMillis()
                    }
                }
            } else {
                AttachmentPreview(
                    onAttach = {
                        if (permissionsState.allPermissionsGranted) {
                            handleOnAttach()
                        } else {
                            timestamp = System.currentTimeMillis()
                        }
                    },
                    onLoad = { thumbnail[it] },
                    onRefresh = {
                        viewModel.thumbnail(
                            attachment.value.files[it].thumbnail,
                            attachment.value.media
                        )
                    },
                    onRemove = { index ->
                        val removed = attachment.value.files[index]
                        attachment.value = UiAttachment.File(
                            attachment.value.media,
                            attachment.value.files - removed
                        )

                        if (imageToCrop.value == removed.uri) {
                            imageToCrop.value = null
                        }
                    },
                    attachment = attachment
                )
            }

            if (imageToCrop.value == null && attachment.value.files.isNotEmpty()) {
                imageToCrop.value = attachment.value.files.first().uri
            }

            Spacer(modifier = Modifier.height(16.dp))

            AnimatedVisibility(visible = imageToCrop.value != null) {
                CropRatioSelection(
                    onSquareClick = {
                        selectedRatio.value = CropRatio.Square
                        shouldLaunchCrop.value = true
                    },
                    onPortraitClick = {
                        selectedRatio.value = CropRatio.Portrait
                        shouldLaunchCrop.value = true
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    LaunchedEffect(permissionsState.allPermissionsGranted) {
        snapshotFlow { timestamp }
            .debounce(500L)
            .collect { stamp ->
                stamp?.run {
                    if (counter > 1 && !permissionsState.allPermissionsGranted) {
                        usecase()
                    } else if (!permissionsState.allPermissionsGranted) {
                        viewModel.updatePermissionStatus()
                        permissionsState.launchMultiplePermissionRequest()
                    } else {
                        handleOnAttach()
                    }
                }
            }
    }

    LaunchedEffect(Unit) {
        viewModel.initialize()
    }

    PhotoEditor(
        imageUri = imageToCrop.value,
        selectedRatio = selectedRatio.value,
        launch = shouldLaunchCrop.value,
        onLaunched = { shouldLaunchCrop.value = false },
        onCropDone = { croppedFile ->
            val uri = croppedFile.uri
            val croppedUiFile = UiFile(uri = uri, thumbnail = uri.toString())

            attachment.value = UiAttachment.File(
                UiMimeType.Photo,
                listOf(croppedUiFile)
            )

            imageToCrop.value = null
        }
    )
}

enum class CropRatio(val x: Float, val y: Float) {
    Square(1f, 1f),
    Portrait(4f, 5f)
}



