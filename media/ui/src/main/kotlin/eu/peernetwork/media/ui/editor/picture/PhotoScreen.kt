package eu.peernetwork.media.ui.editor.picture

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import com.yalantis.ucrop.UCrop
import eu.peernetwork.media.core.model.UiFile
import eu.peernetwork.media.ui.activity.CropActivity
import java.io.File
import java.util.UUID

@Composable
fun PhotoScreen(
    state: MutableState<Long>,
    imageUri: Uri?,
    selectedRatio: PhotoAspectRatio,
    onCropDone: (UiFile) -> Unit
) {
    val context = LocalContext.current
    var lastState = remember { mutableLongStateOf(state.value) }
    val handleCrop by rememberUpdatedState(onCropDone)
    val cropLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        UCrop.getOutput(result.data ?: return@rememberLauncherForActivityResult)?.let {
            val file = File(it.path ?: return@let)
            if (file.exists()) {
                handleCrop(UiFile(it))
            } else {
                Log.e("Crop", "UCrop returned missing file: $it")
            }
        }
    }
    LaunchedEffect(state.value) {
        if (lastState.longValue != state.value && imageUri != null) {
            val destination = Uri.fromFile(File(context.cacheDir, "cropped_${UUID.randomUUID()}.jpg"))
            val intent = UCrop.of(imageUri, destination)
                .withAspectRatio(selectedRatio.x, selectedRatio.y)
                .withOptions(UCrop.Options().apply {
                    setCompressionFormat(Bitmap.CompressFormat.JPEG)
                    setCompressionQuality(90)
                    setFreeStyleCropEnabled(false)
                    setHideBottomControls(false)
                }).getIntent(context).setClass(context, CropActivity::class.java)
            lastState.longValue = state.value
            cropLauncher.launch(intent)
        }
    }
}

enum class PhotoAspectRatio(val x: Float, val y: Float) {
    Square(1f, 1f),
    Portrait(4f, 5f)
}
