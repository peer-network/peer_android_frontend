package eu.peernetwork.social.ui.feedback

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.ui.platform.LocalContext

@Composable
fun FeedbackScreen(
    session: State<Long>,
    appPackage: String
) {
    val context = LocalContext.current
    LaunchedEffect(session.value) {
        if (session.value != -1L) {
            try {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=$appPackage")
                )
                context.startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=$appPackage")
                )
                context.startActivity(intent)
            }
        }
    }
}
