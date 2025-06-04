package eu.peernetwork.app.ui.privacy

import android.annotation.SuppressLint
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.AnimationConstants.DefaultDurationMillis
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.viewinterop.AndroidView
import kotlinx.coroutines.delay

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
    val link = remember { mutableStateOf<String?>(null) }
    Column(
        modifier = Modifier
            .fillMaxSize()
              .systemBarsPadding()
    ) {
        Box(modifier = Modifier.fillMaxSize().clipToBounds()){
            Crossfade(link.value) { target ->
                target?.let {
                    AndroidView(
                        factory = { webView.also { webView.loadUrl(target) } },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
    LaunchedEffect(url) {
        delay(DefaultDurationMillis.toLong())
        link.value = url
    }
}
