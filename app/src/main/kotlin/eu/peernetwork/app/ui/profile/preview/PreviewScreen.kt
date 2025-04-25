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
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
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
import eu.peernetwork.core.ui.design.component.DesignRefreshableScaffold
import eu.peernetwork.core.ui.design.component.DesignStatefulScaffoldState
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.media.core.model.MimeType
import eu.peernetwork.user.ui.user.UserEvent
import eu.peernetwork.user.ui.user.UserScreen

@Composable
fun PreviewScreen(
    userId: String,
    onSettings: () -> Unit,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(
            eu.peernetwork.app.ui.profile.preview.Preview.Builder::class.java
        ).build(context)
    }
    val pageState = rememberSaveable { mutableIntStateOf(0) }
    var isProfileRefreshing = remember { mutableStateOf(false) }
    var isImageRefreshing = remember { mutableStateOf(false) }
    var isVideoRefreshing = remember { mutableStateOf(false) }
    val derivedState = remember { derivedStateOf { DesignStatefulScaffoldState.Success(Unit) } }
    DesignRefreshableScaffold<Unit>(
        state = derivedState,
        modifier = Modifier.fillMaxSize(),
        onRefresh = {
            isImageRefreshing.value = true
            isVideoRefreshing.value = true
            isProfileRefreshing.value = true
        }
    ) {
        PreviewScreen(
            state = pageState,
            header = {
                UserScreen(
                    loadState = isProfileRefreshing,
                    onEvent = {
                        if (it is UserEvent.Settings) {
                            onSettings()
                        }
                    },
                    provider = component,
                    viewModelStoreOwner = viewModelStoreOwner,
                    modifier = Modifier.padding(bottom = 8.dp)
                        .padding(end = 16.dp, start = 24.dp)
                )
            }
        ) { offset ->
            when (offset) {
                0 -> PhotoScreen(
                    userId,
                    BuildConfig.PAGING_LIMIT,
                    isImageRefreshing,
                    component,
                    viewModelStoreOwner
                )
                1 -> VideoScreen(
                    userId,
                    BuildConfig.PAGING_LIMIT,
                    isVideoRefreshing,
                    component,
                    viewModelStoreOwner
                )
                2 -> MusicScreen(component, viewModelStoreOwner)
            }
        }
    }
}

@Composable
fun PreviewScreen(
    state: MutableIntState,
    modifier: Modifier = Modifier,
    onNavigate: (Int) -> Unit = {},
    header: @Composable (State<Float>) -> Unit,
    content: @Composable (Int) -> Unit
) {
    val pageState = rememberPagerState(
        pageCount = { MimeType.TYPES.size },
        initialPage = state.intValue
    )
    PreviewScaffold(
        header = header,
        modifier = modifier,
        pagerState = pageState
    ) {
        HorizontalPager(
            state = pageState,
            verticalAlignment = Alignment.Top,
        ) { page -> content(page) }
        LaunchedEffect(pageState.currentPage) { onNavigate(pageState.currentPage) }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewPreviewScreen() {
    PeerTheme {
        PreviewScaffold(
            modifier = Modifier.fillMaxSize(),
            header = { Text("Header") },
            pagerState = rememberPagerState { 0 },
            content = {
                Text("Content")
            },
        )
    }
}
