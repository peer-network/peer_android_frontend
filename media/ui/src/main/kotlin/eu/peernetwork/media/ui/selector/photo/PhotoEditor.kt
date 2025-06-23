package eu.peernetwork.media.ui.selector.photo

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.yalantis.ucrop.UCrop
import eu.peernetwork.media.core.model.UiFile
import eu.peernetwork.media.ui.activity.CropActivity
import eu.peernetwork.media.ui.attachment.CropRatio
import java.io.File
import java.util.UUID

@Composable
fun PhotoEditor(
    imageUri: Uri?,
    selectedRatio: CropRatio,
    launch: Boolean,
    onLaunched: () -> Unit,
    onCropDone: (UiFile) -> Unit
) {
    if (imageUri == null) return

    val context = LocalContext.current
    var isLaunched by remember(imageUri) { mutableStateOf(false) }

    val cropLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        UCrop.getOutput(result.data ?: return@rememberLauncherForActivityResult)?.let {
            val file = File(it.path ?: return@let)
            if (file.exists()) {
                onCropDone(UiFile(it, it.toString()))
            } else {
                Log.e("Crop", "UCrop returned missing file: $it")
            }
        }
        isLaunched = false
    }

    LaunchedEffect(imageUri, selectedRatio, launch) {
        if (launch && !isLaunched) {
            isLaunched = true
            onLaunched()

            val destination = Uri.fromFile(File(context.cacheDir, "cropped_${UUID.randomUUID()}.jpg"))
            val intent = UCrop.of(imageUri, destination)
                .withAspectRatio(selectedRatio.x, selectedRatio.y)
                .withOptions(UCrop.Options().apply {
                    setCompressionFormat(Bitmap.CompressFormat.JPEG)
                    setCompressionQuality(90)
                    setFreeStyleCropEnabled(false)
                    setHideBottomControls(false)
                })
                .getIntent(context).setClass(context, CropActivity::class.java)

            cropLauncher.launch(intent)
        }
    }
}



