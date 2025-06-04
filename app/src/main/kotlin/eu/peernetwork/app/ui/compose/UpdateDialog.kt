package eu.peernetwork.app.ui.compose

import android.content.Intent
import android.net.Uri
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.platform.LocalContext

@Composable
fun UpdateDialog(state: State<Boolean>, uri: Uri) {
    val context = LocalContext.current
    if (state.value) {
        AlertDialog(
            onDismissRequest = {  },
            title = { Text("Update Required") },
            text = { Text("This version of the app is outdated. Please update to continue.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        context.startActivity(intent)
                    }
                ) { Text("Update Now") }
            },
            dismissButton = {},
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    }
}
