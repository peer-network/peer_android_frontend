package eu.peernetwork.blog.ui.author

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.compose.DesignAvatar
import eu.peernetwork.core.ui.extension.builder

@Composable
fun AuthorScreen(
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Author.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = AuthorViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    DesignAvatar(modifier = modifier) {
        Box(modifier = Modifier
            .size(48.dp)
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
        ) {
            Crossfade(state) { target ->
                when(target) {
                    is AuthorViewModel.State.Success -> AsyncImage(
                        model = target.account.imageUrl,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        contentDescription = null,
                    )
                    else -> {}
                }
            }
        }
    }
}

