package eu.peernetwork.media.ui.selector.explorer

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.compose.DesignBottomSheet
import eu.peernetwork.core.ui.design.compose.DesignDropDown
import eu.peernetwork.core.ui.design.compose.DesignOutlinedButton
import eu.peernetwork.core.ui.design.compose.DesignOverlayBackground
import eu.peernetwork.core.ui.design.compose.DesignTitle
import eu.peernetwork.core.ui.design.compose.DesignTitleBarHost
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.model.ViewModelState
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.media.core.model.UiAttachment
import eu.peernetwork.media.core.model.UiMimeType
import eu.peernetwork.media.ui.R
import eu.peernetwork.media.ui.selector.directory.DirectoryScreen
import eu.peernetwork.media.ui.selector.photo.PhotoScreen
import eu.peernetwork.media.ui.selector.video.VideoScreen
import kotlinx.collections.immutable.persistentListOf

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ExplorerScreen(
    attachment: MutableState<UiAttachment>,
    onFinish: () -> Unit,
    provider: UiComponentProvider
) {
    val context = LocalContext.current
    val viewModelStore = remember { ViewModelState() }
    val component = remember { provider.builder(Explorer.Builder::class.java).build(context) }
    val default = stringResource(R.string.photo_label)
    val title = rememberSaveable { mutableStateOf(default) }
    val directory = rememberSaveable { mutableStateOf<String?>(null) }
    val showDirectory = rememberSaveable { mutableStateOf<Boolean>(false) }
    ExplorerScreen(
        title = title,
        onFinish = onFinish,
        attachment = attachment,
        onClick = { showDirectory.value = true },
        onSelect = {
            directory.value = null
            attachment.value = UiAttachment.File(it, persistentListOf()) },
    ) { type ->
        val tag = directory.value ?: type.id.toString()
        when(type) {
            UiMimeType.Video -> VideoScreen(
                type = type,
                directory,
                attachment,
                component,
                viewModelStore.get(tag)
            )
            else -> PhotoScreen(
                type = type,
                directory,
                attachment,
                component,
                viewModelStore.get(tag)
            )
        }
        DesignBottomSheet(
            tag = "ExplorerScreen/DesignBottomSheet",
            showSheet = showDirectory,
            background = {
                DesignOverlayBackground(
                    showDirectory,
                    modifier = Modifier.fillMaxSize()
                        .background(MaterialTheme.colorScheme.background.copy(alpha = .8f))
                )
            },
        ) {
            DirectoryScreen(
                type = type,
                onSelect = {
                    directory.value = it
                    showDirectory.value = false },
                provider = component,
                viewModelStoreOwner = viewModelStore.get(tag),
            )
        }
        DesignTitleBarHost("ExplorerScreen") {
            titleBar {
                DesignTitle {
                    Text(stringResource(R.string.gallery))
                }
            }
        }
    }
}

@Composable
fun ExplorerScreen(
    title: MutableState<String>,
    attachment: MutableState<UiAttachment>,
    onClick: () -> Unit,
    onFinish: () -> Unit,
    onSelect: (UiMimeType) -> Unit,
    content: @Composable (UiMimeType) -> Unit
) {
    val handleOnClick by rememberUpdatedState(onClick)
    val updatedContent by rememberUpdatedState(content)
    Column {
        val border = MaterialTheme.colorScheme.surfaceVariant
        var expanded = remember { mutableStateOf(false) }
        val photo = stringResource(R.string.photo_label)
        val video = stringResource(R.string.video_label)
        val files = stringResource(R.string.file_label)
        val type = remember(title) { derivedStateOf {
            if (title.value == video) {
                UiMimeType.Video
            } else {
                UiMimeType.Photo
            }
        } }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                val strokeWidth = 1.dp.toPx()
                val y = size.height - strokeWidth / 2
                drawLine(
                    color = border,
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = strokeWidth
                )
            }
            .padding(vertical = 12.dp, horizontal = 24.dp)) {
            DesignDropDown(
                expanded,
                default = title.value,
                modifier = Modifier.padding(top = 4.dp)
                    .clip(RoundedCornerShape(16))
                    .background(color = border)
            ) {
                item(tag = photo, {
                    title.value = photo
                    onSelect(UiMimeType.Photo)
                    true
                }) { label, isActive ->
                    Text(
                        label,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    )
                }
                item(tag = video, {
                    title.value = video
                    onSelect(UiMimeType.Video)
                    true
                }) { label, isActive ->
                    Text(
                        label,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    )
                }
                item(tag = files, {
                    handleOnClick()
                    expanded.value = false
                    false
                }) { label, isActive ->
                    Text(
                        label,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            if (attachment.value.files.isNotEmpty()) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(28.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(MaterialTheme.colorScheme.onBackground)
                ) {
                    Text(
                        "${attachment.value.files.size}",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = MaterialTheme.colorScheme.background,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            DesignOutlinedButton(
                onClick = onFinish,
                modifier = Modifier.background(
                    color = border,
                    shape = RoundedCornerShape(28),
                ),
                enabled = attachment.value.files.isNotEmpty(),
                shape = RoundedCornerShape(28),
                textStyle = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onBackground
                ),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.onBackground,
                    disabledContainerColor = Color.Transparent
                ),
                minHeight = 32.dp,
                border = BorderStroke(1.dp, border),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)
            ) { Text(stringResource(R.string.done_label)) }
        }
        updatedContent(type.value)
    }
}

@Preview
@Composable
fun PreviewExplorerScreen() {
    PeerTheme {
        val photo = stringResource(R.string.photo_label)
        ExplorerScreen(
            remember { mutableStateOf(photo) },
            remember { mutableStateOf(UiAttachment.Text) },
            {},
            {},
            {}
        ) { Box(modifier = Modifier.fillMaxSize()) }
    }
}
