package eu.peernetwork.social.ui.feedback

import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.ui.platform.LocalContext

@Composable
fun FeedbackScreen(
    session: State<Long>
) {
    val context = LocalContext.current
    LaunchedEffect(session.value) {
        if (session.value != -1L) {
            val formUrl = "https://docs.google.com/forms/d/e/1FAIpQLSeTRecbfUTKmpYHSaE7bSawEagUpkOPagJtLqZdsec659HaGw/viewform"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(formUrl))
            context.startActivity(intent)
        }
    }
}

