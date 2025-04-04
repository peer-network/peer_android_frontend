package eu.peernetwork.blog.ui.point

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.blog.ui.model.UiPoint
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.extension.builder

@Composable
fun UserPointScreen(
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(BlogPoint.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = BlogPointViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val points = remember { mutableStateOf<List<UiPoint>?>(
        (state as? BlogPointViewModel.State.Success?)?.points
    ) }
    Crossfade(targetState = points.value) {
        when (it) {
            null -> Box {}
            else -> UserPointBadge(it)
        }
    }
    LaunchedEffect(state) {
        when (state) {
            is BlogPointViewModel.State.Initialize -> viewModel.getPoints()
            is BlogPointViewModel.State.Loading -> points.value = null
            is BlogPointViewModel.State.Success -> {
                points.value = (state as BlogPointViewModel.State.Success).points
            }
            is BlogPointViewModel.State.Error -> {}
        }
    }
}
