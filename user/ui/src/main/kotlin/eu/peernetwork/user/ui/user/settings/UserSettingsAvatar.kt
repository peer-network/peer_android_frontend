package eu.peernetwork.user.ui.user.settings

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.yalantis.ucrop.UCrop
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.compose.DesignAvatar
import eu.peernetwork.core.ui.theme.PeerAppGreen
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.user.ui.user.compose.UserAvatar
import java.io.File

@Composable
fun UserSettingsAvatar(
    name: String,
    imageUrl: String,
    onChange: (Uri?) -> Unit = {}
) {
    val context = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val imageCropLauncher = rememberLauncherForActivityResult(
        contract = object : ActivityResultContract<Pair<Uri, Uri>, Uri?>() {
            override fun createIntent(context: Context, input: Pair<Uri, Uri>): Intent {
                return UCrop.of(input.first, input.second).withAspectRatio(1f, 1f).getIntent(context)
            }

            override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
                return if (resultCode == Activity.RESULT_OK) {
                    UCrop.getOutput(intent!!)
                } else null
            }
        }
    ) { it?.let {
        imageUri = it
        onChange(it)
    } }
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { it?.let {
        val fileName = "cropped_${System.currentTimeMillis()}.jpg"
        val destinationUri = Uri.fromFile(File(context.cacheDir, fileName))
        imageCropLauncher.launch(it to destinationUri)
    } }
    DesignAvatar(
        icon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_edit),
                contentDescription = null,
                modifier = Modifier
                    .background(PeerAppGreen, CircleShape)
                    .border(1.dp, MaterialTheme.colorScheme.background, CircleShape)
                    .padding(6.dp)
                    .size(8.dp)
                    .clickable { imagePickerLauncher.launch("image/*") }
            )
        },
        modifier = Modifier.padding(end = 8.dp)
    ) {
        imageUri?.let {
            Image(
                painter = rememberAsyncImagePainter(it),
                modifier = Modifier.size(64.dp),
                contentDescription = null
            )
        } ?: UserAvatar(name, imageUrl)
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewUserSettingsAvatar() {
    PeerTheme {
        UserSettingsAvatar("John Doe", "")
    }
}
