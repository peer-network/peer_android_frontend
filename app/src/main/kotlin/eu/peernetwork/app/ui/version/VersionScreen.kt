package eu.peernetwork.app.ui.version

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import eu.peernetwork.app.BuildConfig
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.factory.UiViewModelStore

@Composable
fun VersionScreen(
    version: String,
    versionCode: Int,
    provider: UiComponentProvider,
    viewModelStore: UiViewModelStore,
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Version.Builder::class.java).build(context)
    }
    VersionPage(
        version = version,
        versionCode = versionCode,
        onAppWikiClicked = {
            val intent = Intent(Intent.ACTION_VIEW, BuildConfig.APP_WIKI.toUri())
            context.startActivity(intent)
        }
    ) {
        val intent = Intent(Intent.ACTION_VIEW, BuildConfig.BACKEND_WIKI.toUri())
        context.startActivity(intent)
    }
}
