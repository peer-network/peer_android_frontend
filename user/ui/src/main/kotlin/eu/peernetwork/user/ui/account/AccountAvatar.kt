package eu.peernetwork.user.ui.account

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
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.yalantis.ucrop.UCrop
import eu.peernetwork.core.ui.design.material.DesignAvatar
import eu.peernetwork.core.ui.theme.PeerAppGreen
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.user.ui.R
import eu.peernetwork.user.ui.model.UiAccount
import eu.peernetwork.user.ui.model.UiMetric
import eu.peernetwork.core.ui.design.luna.DesignImage
import eu.peernetwork.user.ui.activity.CropActivity
import java.io.File

@Composable
fun AccountAvatar(
    name: String,
    imageUrl: String,
    onChange: (Uri?) -> Unit = {}
) {
    val context = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val avatarHandler by rememberUpdatedState(onChange)
    val imageCropLauncher = rememberLauncherForActivityResult(
        contract = object : ActivityResultContract<Pair<Uri, Uri>, Uri?>() {
            override fun createIntent(context: Context, input: Pair<Uri, Uri>): Intent {
                return UCrop.of(input.first, input.second).withAspectRatio(1f, 1f)
                    .getIntent(context).setClass(context, CropActivity::class.java)
            }

            override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
                return if (resultCode == Activity.RESULT_OK) {
                    UCrop.getOutput(intent!!)
                } else null
            }
        }
    ) { it?.let {
        imageUri = it
        avatarHandler(it)
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
                painter = painterResource(id = eu.peernetwork.core.ui.R.drawable.ic_edit),
                contentDescription = stringResource(R.string.profile_image),
                tint = MaterialTheme.colorScheme.onBackground,
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
                contentDescription = null,
                modifier = Modifier.size(88.dp),
            )
        } ?: DesignImage(
            label = name,
            imageUrl = imageUrl,
            size = 88.dp
        )
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewAccountAvatar() {
    PeerTheme {
        val model = UiAccount(
            id = System.currentTimeMillis().toString(),
            username = "John Doe",
            slug = 0,
            bio = "Description....",
            imageUrl = "",
            metric = UiMetric(
                posts = 0,
                peers = 0,
                followers = 0,
                followed = 0
            ),
            isFollowing = false,
            isFollowed = false
        )
        AccountAvatar(
            name = model.username,
            imageUrl = model.imageUrl,
            onChange = {  }
        )
    }
}
