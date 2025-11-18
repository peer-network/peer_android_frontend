package eu.peernetwork.ads.ui.boost

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.ads.ui.R
import eu.peernetwork.ads.ui.article.ArticleScreen
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.material.DesignTitle
import eu.peernetwork.core.ui.design.material.DesignTitleBarHost
import eu.peernetwork.core.ui.extension.builder

@Composable
fun BoostScreen(
    id: String,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    val context = LocalContext.current
    val component = remember { provider.builder(Boost.Builder::class.java).build(context) }
    ArticleScreen(
        id = id,
        provider = component,
        viewModelStoreOwner = viewModelStoreOwner
    ) {
        BoostPage(
            onNext = onNext,
            onBack = onBack,
        )
    }
    DesignTitleBarHost("BoostScreen$id", {}) {
        titleBar {
            DesignTitle {
                Text(stringResource(R.string.boost_label))
            }
        }
    }
}
