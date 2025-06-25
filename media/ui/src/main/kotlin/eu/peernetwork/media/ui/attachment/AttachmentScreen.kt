package eu.peernetwork.media.ui.attachment

import android.Manifest
import android.os.Build
import androidx.compose.animation.Crossfade
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.media.core.model.UiAttachment
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
                    {
                        if (permissionsState.allPermissionsGranted) {
                            handleOnAttach()
                        } else {
                            timestamp = System.currentTimeMillis()
                        }
                    },
                    { thumbnail[it] },
                    { viewModel.thumbnail(
                        attachment.value.files[it].thumbnail,
                        attachment.value.media
                    ) },
                    attachment
                )
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
}

