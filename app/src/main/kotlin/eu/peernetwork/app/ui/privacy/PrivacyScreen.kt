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
import eu.peernetwork.app.BuildConfig

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun PrivacyScreen(onBack: () -> Unit) {
    BackHandler(onBack = onBack)
    val context = LocalContext.current
    var webView by remember { mutableStateOf(WebView(context).apply {
        settings.javaScriptEnabled = true
        webViewClient = WebViewClient()
         }
       )
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
//            .navigationBarsPadding()
//            .statusBarsPadding()
              .systemBarsPadding()
    ) {
        Box(modifier = Modifier.fillMaxSize().clipToBounds()){
            AndroidView(
                factory = { webView.also {
                            webView.loadUrl(
                            BuildConfig.PRIVACY_POLICY_URL    //BuildConfig created for Privacy policy URL
                            )   //webView.loadUrl("https://www.freeprivacypolicy.com/live/02865c3a-79db-4baf-9ca1-7d91e2cf1724")
                     }
                }
            )
        }
    }
}
