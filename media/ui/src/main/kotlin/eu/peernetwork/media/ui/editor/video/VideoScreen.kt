package eu.peernetwork.media.ui.editor.video

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.media.ui.compose.VideoRange

@Composable
fun VideoScreen(
    path: String,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Video.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = VideoViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    Column(
        modifier = Modifier.fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        val start = remember { mutableLongStateOf(1) }
        val stop = remember { mutableLongStateOf(3) }
        Spacer(modifier = Modifier.height(16.dp))
        VideoRange(
            start,
            stop,
            60,
            5,
            2,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Box(modifier = Modifier.fillMaxWidth()
                .height(64.dp)
                .background(MaterialTheme.colorScheme.tertiary))
        }
    }
}

@Composable
@SuppressLint("UnusedBoxWithConstraintsScope")
fun VideoScreen(
    start: State<Long>,
    stop: State<Long>,
    duration: Long,
    unit: Long,
    modifier: Modifier = Modifier,
    divider: @Composable (Int, Int) -> Unit = { index, size -> },
    mask: @Composable (Float, Float) -> Unit = { start, end -> },
    leading: @Composable (State<Float>) -> Unit,
    trailing: @Composable (State<Float>) -> Unit,
    track: @Composable (State<Float>, State<Float>) -> Unit,
    background: @Composable (Long, Long) -> Unit,
    content: @Composable (Long) -> Unit,
) {

}

@Preview
@Composable
fun PreviewVideoScreen() {
    PeerTheme {
        val start = remember { mutableLongStateOf(2) }
        val stop = remember { mutableLongStateOf(4) }
    }
}
