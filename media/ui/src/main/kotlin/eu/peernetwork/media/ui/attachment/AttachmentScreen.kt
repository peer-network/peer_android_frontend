package eu.peernetwork.media.ui.attachment

import android.Manifest
import android.content.ContentResolver
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.compose.DesignLabel
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.media.core.model.UiAttachment
import eu.peernetwork.media.core.model.UiFile
import eu.peernetwork.media.core.model.UiMimeType
import eu.peernetwork.media.core.R
import eu.peernetwork.media.ui.editor.picture.PhotoAspectRatio
import eu.peernetwork.media.ui.editor.picture.PhotoScreen
import eu.peernetwork.media.ui.usecase.PermissionUsecase
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce

@Composable
@OptIn(ExperimentalPermissionsApi::class, FlowPreview::class)
fun AttachmentScreen(
    attachment: MutableState<UiAttachment>,
    onAttach: () -> Unit,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    modifier: Modifier = Modifier,
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
    val ratio = remember { mutableStateOf<PhotoAspectRatio>(PhotoAspectRatio.Square) }
    val launcher = remember { mutableLongStateOf(System.currentTimeMillis()) }
    AttachmentScreen(
        modifier = modifier,
        attachment = attachment,
        onLoad = { thumbnail[it] },
        onRefresh = {
            viewModel.thumbnail(
                attachment.value.files[it].thumbnail,
                attachment.value.media
            )
        },
        onAttach = {
            if (permissionsState.allPermissionsGranted) {
                handleOnAttach()
            } else {
                timestamp = System.currentTimeMillis()
            }
        },
        onSelect = { imageToCrop.value = attachment.value.files[it].uri },
        onPreview = {
            imageToCrop.value = attachment.value.files[it].uri
            launcher.longValue = System.currentTimeMillis() },
        onSquareClick = {
            ratio.value = PhotoAspectRatio.Square
            launcher.longValue = System.currentTimeMillis() },
        onPortraitClick = {
            ratio.value = PhotoAspectRatio.Portrait
            launcher.longValue = System.currentTimeMillis() },
        onDetach = {
            val removed = attachment.value.files[it]
            attachment.value = UiAttachment.File(
                attachment.value.media,
                attachment.value.files.filterNot {
                    it.uri == removed.uri
                }.toPersistentList()
            )
            if (imageToCrop.value == removed.uri) {
                imageToCrop.value = null
            }
        }
    )
    PhotoScreen(
        state = launcher,
        imageUri = imageToCrop.value,
        selectedRatio = ratio.value,
        onCropDone = {
            attachment.value = UiAttachment.File(
                UiMimeType.Photo,
                persistentListOf(it)
            )
        }
    )
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
    LaunchedEffect(Unit) { viewModel.initialize() }
}

@Composable
fun AttachmentScreen(
    modifier: Modifier = Modifier,
    attachment: MutableState<UiAttachment>,
    onLoad: (String) -> Bitmap?,
    onRefresh: (Int) -> Unit,
    onAttach: () -> Unit,
    onSelect: (Int) -> Unit,
    onPreview: (Int) -> Unit,
    onSquareClick: () -> Unit,
    onPortraitClick: () -> Unit,
    onDetach: (Int) -> Unit,
) {
    val imageToCrop = remember(attachment.value) {
        mutableStateOf<Uri?>(attachment.value.files.firstOrNull()?.uri)
    }
    val isVisible by remember { derivedStateOf {
        imageToCrop.value != null && attachment.value.files.isNotEmpty()
    } }
    DesignLabel(
        modifier = modifier,
        visible = isVisible,
        label = {
            if (attachment.value.media == UiMimeType.Photo) {
                AttachmentOption(
                    onSquareClick = onSquareClick,
                    onPortraitClick = onPortraitClick,
                    modifier = Modifier.fillMaxWidth()
                        .padding(
                            top = 4.dp,
                            bottom = 8.dp
                        )
                )
            }
        }
    ) {
        Box(modifier = Modifier.padding(bottom = 4.dp)) {
            AttachmentPreview(
                onAttach = onAttach,
                onLoad = onLoad,
                onRefresh = onRefresh,
                onRemove = onDetach,
                attachment = attachment,
                onSelect = onSelect,
                onPreview = onPreview
            )
        }
    }
}

@Preview
@Composable
fun PreviewAttachmentScreen() {
    PeerTheme {
        val context = LocalContext.current
        val uri = (ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + context.resources.getResourcePackageName(R.drawable.ic_play) +
                '/' + context.resources.getResourceTypeName(R.drawable.ic_play) +
                '/' + context.resources.getResourceEntryName(R.drawable.ic_play)).toUri()
        val thumbnail = uri.toString()
        val attachment = UiAttachment.File(
            UiMimeType.Photo,
            persistentListOf(UiFile(uri = uri, thumbnail = thumbnail))
        )
        val state = remember { mutableStateOf<UiAttachment>(attachment) }
        AttachmentScreen(
            attachment = state,
            onLoad = { null },
            onRefresh = {},
            onAttach = {},
            onSelect = {},
            onPreview = {},
            onSquareClick = {},
            onPortraitClick = {},
            onDetach = {}
        )
    }
}
