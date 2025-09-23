package eu.peernetwork.app.ui.splash

import android.content.Intent
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.net.toUri
import eu.peernetwork.app.BuildConfig
import eu.peernetwork.app.R

@Composable
fun SplashConfirmation(state: State<Boolean>) {
    val context = LocalContext.current
    if (state.value) {
        AlertDialog(
            onDismissRequest = {  },
            title = { Text(stringResource(R.string.version_update_title)) },
            text = { Text(stringResource(R.string.version_update_message)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, BuildConfig.PLAYSTORE_URL.toUri())
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        context.startActivity(intent)
                    }
                ) { Text(stringResource(R.string.version_update_action)) }
            },
            dismissButton = {},
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    }
}
