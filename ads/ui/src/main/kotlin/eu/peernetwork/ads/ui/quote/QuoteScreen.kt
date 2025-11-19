package eu.peernetwork.ads.ui.quote

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.ads.ui.article.ArticleScreen
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.extension.builder

@Composable
fun QuoteScreen(
    id: String,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    val context = LocalContext.current
    val component = remember { provider.builder(Quote.Builder::class.java).build(context) }
    ArticleScreen(
        id = id,
        provider = component,
        viewModelStoreOwner = viewModelStoreOwner
    ) {
        QuotePage(
            onNext = onNext,
            onBack = onBack,
        )
    }
}
