package eu.peernetwork.social.ui.feed

import android.content.res.Configuration
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.social.ui.content.music.MusicScreen
import eu.peernetwork.social.ui.content.photo.PhotoScreen
import eu.peernetwork.social.ui.content.video.VideoScreen

@Composable
fun FeedScreen(
    page: Int,
    onNavigate: (Int) -> Unit = {},
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Feed.Builder::class.java).build(context)
    }
    FeedContent(
        page = page,
        modifier = Modifier.fillMaxSize(),
        onNavigate = onNavigate,
        photo = { PhotoScreen(component, viewModelStoreOwner) },
        video = { VideoScreen(component, viewModelStoreOwner) },
        music = { MusicScreen(component, viewModelStoreOwner) }
    )
}

@Composable
fun FeedContent(
    page: Int,
    modifier: Modifier = Modifier,
    onNavigate: (Int) -> Unit = {},
    photo: @Composable () -> Unit,
    video: @Composable () -> Unit,
    music: @Composable () -> Unit,
) {
    val state = rememberPagerState(pageCount = { 3 }, initialPage = page)
    HorizontalPager(
        state = state,
        modifier = modifier,
        verticalAlignment = Alignment.Top,
    ) { page ->
        Crossfade(targetState = page) { targetPage ->
            when (targetPage) {
                0 -> photo()
                1 -> video()
                2 -> music()
            }
        }
    }
    LaunchedEffect(page) { state.scrollToPage(page) }
    LaunchedEffect(state.currentPage) { onNavigate(state.currentPage) }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewFeedScreen() {
    PeerTheme {
        FeedContent(
            page = 0,
            modifier = Modifier.fillMaxSize(),
            photo = { Text("Photo") },
            video = { Text("Video") },
            music = { Text("Music") }
        )
    }
}
