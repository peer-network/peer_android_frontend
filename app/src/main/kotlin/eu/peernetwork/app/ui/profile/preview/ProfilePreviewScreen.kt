package eu.peernetwork.app.ui.profile.preview

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import eu.peernetwork.app.BuildConfig
import eu.peernetwork.blog.ui.post.music.MusicScreen
import eu.peernetwork.blog.ui.post.photo.PhotoScreen
import eu.peernetwork.blog.ui.post.video.VideoScreen
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.user.ui.user.UserEvent
import eu.peernetwork.user.ui.user.UserScreen

@Composable
fun ProfilePreviewScreen(
    userId: String,
    onSettings: () -> Unit,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(ProfilePreview.Builder::class.java).build(context)
    }
    val pageState = rememberSaveable { mutableIntStateOf(0) }
    ProfilePreviewContent(
        state = pageState,
        header = {
            UserScreen(
                onEvent = {
                    if (it is UserEvent.Settings) {
                        onSettings()
                    }
                },
                provider = component,
                viewModelStoreOwner = viewModelStoreOwner,
                modifier = Modifier.padding(
                    bottom = 8.dp
                ).padding(end = 16.dp, start = 24.dp)
            )
        }
    ) { offset ->
        when (offset) {
            0 -> PhotoScreen(userId, BuildConfig.PAGING_LIMIT, component, viewModelStoreOwner)
            1 -> VideoScreen(userId, BuildConfig.PAGING_LIMIT, component, viewModelStoreOwner)
            2 -> MusicScreen(component, viewModelStoreOwner)
        }
    }
}

@Composable
fun ProfilePreviewContent(
    state: MutableIntState,
    modifier: Modifier = Modifier,
    onNavigate: (Int) -> Unit = {},
    header: @Composable () -> Unit,
    content: @Composable (Int) -> Unit
) {
    val pageState = rememberPagerState(pageCount = { 3 }, initialPage = state.intValue)
    ProfilePreviewScaffold(header = header) {
        HorizontalPager(
            state = pageState,
            modifier = modifier,
            verticalAlignment = Alignment.Top,
        ) { page -> content(page) }
        LaunchedEffect(pageState.currentPage) { onNavigate(pageState.currentPage) }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewProfilePreview() {
    PeerTheme {
        ProfilePreviewScaffold(
            modifier = Modifier.fillMaxSize(),
            header = { Text("Header") },
            content = {
                Text("Content")
            },
        )
    }
}
