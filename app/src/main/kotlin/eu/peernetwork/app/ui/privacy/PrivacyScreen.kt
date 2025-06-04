package eu.peernetwork.app.ui.privacy

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.viewinterop.AndroidView

@Composable
@SuppressLint("SetJavaScriptEnabled")
fun PrivacyScreen(
    url: String,
    onBack: () -> Unit
) {
    BackHandler(onBack = onBack)
    val context = LocalContext.current
    var webView by remember {
        mutableStateOf(WebView(context).apply {
            settings.javaScriptEnabled = true
            webViewClient = WebViewClient()
        })
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
              .systemBarsPadding()
    ) {
        Box(modifier = Modifier.fillMaxSize().clipToBounds()){
            AndroidView(
                factory = { webView.also { webView.loadUrl(url) } }
            )
        }
    }
}
