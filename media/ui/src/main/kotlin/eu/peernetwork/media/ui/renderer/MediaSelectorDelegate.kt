package eu.peernetwork.media.ui.renderer

import android.Manifest
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.shouldShowRationale
import eu.peernetwork.media.core.renderer.MediaSelector
import eu.peernetwork.media.core.model.MimeType
import eu.peernetwork.media.ui.selector.music.MusicCreate
import eu.peernetwork.media.ui.selector.photo.PhotoCreate
import eu.peernetwork.media.ui.selector.video.VideoCreate
import javax.inject.Inject
import kotlin.collections.any

class MediaSelectorDelegate @Inject constructor(
    private val context: Context
) : MediaSelector {
    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    override fun invoke(modifier: Modifier, spec: MediaSelector.Spec) {
        val permissionsState = rememberMultiplePermissionsState(
            permissions = listOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.READ_MEDIA_VIDEO
            )
        )

        LaunchedEffect(spec.type) {
            if (!permissionsState.allPermissionsGranted && spec.type != null) {
                permissionsState.launchMultiplePermissionRequest()
            }
        }

        val type = spec.type ?: if (permissionsState.permissions.any { it.status.isGranted }) {
            MimeType.Photo
        } else {
            null
        }

        when(type) {
            MimeType.Music -> MusicCreate()
            MimeType.Video -> VideoCreate(spec.attachments) {}
            MimeType.Photo -> PhotoCreate(spec.attachments, onPhotosSelected = {})
            else -> {}
        }
    }
}
