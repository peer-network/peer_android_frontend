package eu.peernetwork.app.ui.feed

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.app.BuildConfig
import eu.peernetwork.media.core.model.MimeType
import eu.peernetwork.blog.ui.timeline.music.MusicScreen
import eu.peernetwork.blog.ui.timeline.photo.PhotoScreen
import eu.peernetwork.blog.ui.timeline.video.VideoScreen
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.compose.DesignTab
import eu.peernetwork.core.ui.design.compose.DesignToolbarTitle
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.extension.navigateIfNecessary
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
fun FeedScreen(
    id: String,
    title: MutableState<DesignToolbarTitle>,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Feed.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = FeedViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val pageState = remember { mutableIntStateOf(state.page) }
    FeedNavigation(
        id = id,
        title = title,
        component = component,
        viewModelStoreOwner = viewModelStoreOwner
    ) { controller ->
        FeedScreen(
            state = pageState,
            modifier = Modifier.fillMaxSize(),
            onNavigate = { viewModel.lastVisited(it) },
            photo = { PhotoScreen(id, BuildConfig.PAGING_LIMIT, component, viewModelStoreOwner) {
                controller.navigateIfNecessary("profile/$it")
            } },
            video = { VideoScreen(id, BuildConfig.PAGING_LIMIT, component, viewModelStoreOwner) {
                controller.navigateIfNecessary("profile/$it")
            } },
            music = { MusicScreen(component, viewModelStoreOwner) }
        )
        LaunchedEffect(Unit) {
            title.value = DesignToolbarTitle(R.string.home_label) {}
        }
    }
}

@Composable
fun FeedScreen(
    state: MutableIntState,
    modifier: Modifier = Modifier,
    onNavigate: (Int) -> Unit = {},
    photo: @Composable () -> Unit,
    video: @Composable () -> Unit,
    music: @Composable () -> Unit,
) {
    val pageState = rememberPagerState(
        pageCount = { MimeType.TYPES.size },
        initialPage = state.intValue
    )
    Column {
        DesignTab(pageState) { index ->
            MimeType.TYPES[index].let {
                Icon(
                    painter = painterResource(id = it.id),
                    contentDescription = it.label?.let { stringResource(it) },
                    tint = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .size(28.dp)
                )
            }
        }
        HorizontalPager(
            state = pageState,
            modifier = modifier,
            verticalAlignment = Alignment.Top,
        ) { page ->
            when (page) {
                0 -> photo()
                1 -> video()
                2 -> music()
            }
        }
    }
    LaunchedEffect(pageState.currentPage) { onNavigate(pageState.currentPage) }
}

@Composable
@Preview
fun PreviewFeedScreen() {
    val state = rememberSaveable { mutableIntStateOf(0) }
    PeerTheme {
        FeedScreen(
            state = state,
            modifier = Modifier.fillMaxSize(),
            photo = { Text("Photo") },
            video = { Text("Video") },
            music = { Text("Music") }
        )
    }
}
