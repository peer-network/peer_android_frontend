package eu.peernetwork.social.ui.feedback

import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri

@Composable
fun FeedbackScreen(
    session: State<Long>,
    url: String
) {
    val context = LocalContext.current
    LaunchedEffect(session.value) {
        if (session.value != -1L) {
            val intent = Intent(Intent.ACTION_VIEW, url.toUri())
            context.startActivity(intent)
        }
    }
}
