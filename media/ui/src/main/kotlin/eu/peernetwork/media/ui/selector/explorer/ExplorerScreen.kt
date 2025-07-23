package eu.peernetwork.media.ui.selector.explorer

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.compose.DesignCollapsibleBottomSheet
import eu.peernetwork.core.ui.design.compose.DesignDropDown
import eu.peernetwork.core.ui.design.compose.DesignTitle
import eu.peernetwork.core.ui.design.compose.DesignTitleBarHost
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.model.ViewModelState
import eu.peernetwork.media.core.model.UiAttachment
import eu.peernetwork.media.core.model.UiMimeType
import eu.peernetwork.media.ui.R
import eu.peernetwork.media.ui.compose.NudgeButton
import eu.peernetwork.media.ui.saveable.UiAttachmentSaver
import eu.peernetwork.media.ui.selector.audio.AudioScreen
import eu.peernetwork.media.ui.selector.directory.DirectoryScreen
import eu.peernetwork.media.ui.selector.photo.PhotoScreen
import eu.peernetwork.media.ui.selector.video.VideoScreen

@Composable
fun ExplorerScreen(
    attachment: State<UiAttachment>,
    provider: UiComponentProvider,
    onFinish: (UiAttachment) -> Unit,
) {
    val context = LocalContext.current
    val viewModelStore = remember { ViewModelState() }
    val component = remember { provider.builder(Explorer.Builder::class.java).build(context) }
    val default = stringResource(R.string.photo_label)
    val title = rememberSaveable { mutableStateOf(default) }
    val directory = rememberSaveable { mutableStateOf<String?>(null) }
    val showDirectory = rememberSaveable { mutableStateOf<Boolean>(false) }
    val selected = rememberSaveable(saver = UiAttachmentSaver) { mutableStateOf<UiAttachment>(attachment.value) }
    val handleFinish by rememberUpdatedState(onFinish)
    ExplorerScreen(
        title = title,
        onFinish = { handleFinish(selected.value) },
        attachment = selected,
        onDirectoryChange = { showDirectory.value = true },
        onTypeChange = { directory.value = null },
    ) { type ->
        val tag = directory.value ?: type.id.toString()
        when(type) {
            UiMimeType.Video -> VideoScreen(
                directory,
                attachment,
                component,
                viewModelStore.get(tag)
            ) { selected.value = it }

            UiMimeType.Photo -> PhotoScreen(
                directory,
                attachment,
                component,
                viewModelStore.get(tag)
            ) { selected.value = it }

            UiMimeType.Music -> AudioScreen(
                type = type,
                directory = directory,
                attachment = attachment,
                provider = component,
                viewModelStoreOwner = viewModelStore.get(tag)
            ) { selected.value = it }

            else -> {

            }
        }
        DesignCollapsibleBottomSheet(
            state = showDirectory,
            peekHeight = 400.dp,
            onDismiss = { showDirectory.value = false }
        ) {
            Box(modifier = Modifier.statusBarsPadding()) {
                DirectoryScreen(
                    type = type,
                    onSelect = {
                        directory.value = it
                        showDirectory.value = false },
                    provider = component,
                    viewModelStoreOwner = viewModelStore.get(tag),
                )
            }
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
    attachment: State<UiAttachment>,
    onFinish: () -> Unit,
    onTypeChange: (UiMimeType) -> Unit,
    onDirectoryChange: () -> Unit,
    content: @Composable (UiMimeType) -> Unit
) {
    val handleOnClick by rememberUpdatedState(onDirectoryChange)
    val updatedContent by rememberUpdatedState(content)
    val state = remember(attachment.value) { mutableStateOf(attachment.value.files.isNotEmpty()) }
    Column {
        val border = MaterialTheme.colorScheme.surfaceVariant
        val expanded = remember { mutableStateOf(false) }
        val photo = stringResource(R.string.photo_label)
        val video = stringResource(R.string.video_label)
        val audio = stringResource(R.string.audio_label)
        val files = stringResource(R.string.file_label)
        val type = remember(title) { derivedStateOf {
            if (title.value == video) {
                UiMimeType.Video
            } else if (title.value == photo) {
                UiMimeType.Photo
            } else  {
                UiMimeType.Music
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
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
            ) {
                val items = mapOf(
                    photo to eu.peernetwork.core.ui.R.drawable.ic_photo,
                    video to eu.peernetwork.core.ui.R.drawable.ic_video,
                    audio to eu.peernetwork.core.ui.R.drawable.ic_music,
                    files to eu.peernetwork.core.ui.R.drawable.ic_wallet
                )

                items.entries.forEach { (label, iconRes) ->
                    item(tag = label, {
                        when (label) {
                            photo -> {
                                title.value = photo
                                onTypeChange(UiMimeType.Photo)
                            }
                            video -> {
                                title.value = video
                                onTypeChange(UiMimeType.Video)
                            }
                            audio -> {
                                title.value = audio
                                onTypeChange(UiMimeType.Music)
                            }
                            files -> {
                                handleOnClick()
                                expanded.value = false
                            }
                        }
                        true
                    }) { _, isActive ->
                        Spacer(modifier = Modifier.height(2.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .padding(start = 6.dp)
                                .padding(vertical = 6.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = iconRes),
                                contentDescription = label,
                                modifier = Modifier.size(20.dp),
                                tint = if (isActive)
                                    MaterialTheme.colorScheme.onBackground
                                else
                                    MaterialTheme.colorScheme.tertiary
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                label,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = if (isActive)
                                        MaterialTheme.colorScheme.onBackground
                                    else
                                        MaterialTheme.colorScheme.tertiary
                                )
                            )
                            if (isActive) {
                                Spacer(modifier = Modifier.width(2.dp))
                                Icon(
                                    painter = painterResource(id = eu.peernetwork.core.ui.R.drawable.ic_caret_down),
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                            } else {
                                Spacer(modifier = Modifier.width(12.dp))
                            }
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            AnimatedVisibility(state.value) {
                NudgeButton(
                    count = attachment.value.files.size,
                    onClick = onFinish,
                    color = border,
                    enabled = state.value
                ) {
                    Text(
                        stringResource(R.string.proceed_label),
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            }
        }
        updatedContent(type.value)
    }
}

@Preview
@Composable
fun PreviewExplorerScreen() {
    val photo = stringResource(R.string.photo_label)
    ExplorerScreen(
        title = remember { mutableStateOf(photo) },
        attachment = remember { mutableStateOf(UiAttachment.Text) },
        onDirectoryChange = {},
        onFinish = {},
        onTypeChange = {},
        content = {}
    )
}
