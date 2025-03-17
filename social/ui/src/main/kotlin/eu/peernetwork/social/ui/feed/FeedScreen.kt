package eu.peernetwork.social.ui.feed

import android.content.res.Configuration
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.extension.attachIfNecessary
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.theme.PeerTheme
import eu.peernetwork.social.ui.content.music.MusicScreen
import eu.peernetwork.social.ui.content.photo.PhotoScreen
import eu.peernetwork.social.ui.content.video.VideoScreen

@Composable
fun FeedScreen(
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner
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
    val controller = rememberNavController()
    val state by viewModel.state.collectAsStateWithLifecycle()
    LaunchedEffect(state) {
        when(state) {
            is FeedViewModel.State.Initialize -> controller.attachIfNecessary("initialize")
            is FeedViewModel.State.Ready -> {
                val page = (state as FeedViewModel.State.Ready).page
                controller.attachIfNecessary("content/$page")
            }
        }
    }
    FeedScaffold {
        NavHost(navController = controller, startDestination = "initialize") {
            composable("initialize") { }
            composable(
                "content/{page}",
                arguments = listOf(navArgument("page") { type = NavType.IntType })
            ) {
                FeedPager(
                    page = it.arguments?.getInt("page") ?: 0,
                    modifier = Modifier.fillMaxSize(),
                    onNavigate = { viewModel.updateFeed(it) },
                    photo = { PhotoScreen(provider, viewModelStoreOwner) },
                    video = { VideoScreen(provider, viewModelStoreOwner) },
                    music = { MusicScreen(provider, viewModelStoreOwner) }
                )
            }
        }
    }
}

@Composable
private fun FeedPager(
    page: Int,
    modifier: Modifier = Modifier,
    onNavigate: (Int) -> Unit = {},
    photo: @Composable () -> Unit,
    video: @Composable () -> Unit,
    music: @Composable () -> Unit,
) {
    val pager = rememberPagerState(pageCount = { 3 }, initialPage = page)
    HorizontalPager(
        state = pager,
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
    LaunchedEffect(page) { pager.scrollToPage(page) }
    LaunchedEffect(pager.currentPage) { onNavigate(pager.currentPage) }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewFeedScreen() {
    PeerTheme {
        FeedScaffold {
            FeedPager(
                page = 0,
                modifier = Modifier.fillMaxSize(),
                photo = { Text("Photo") },
                video = { Text("Video") },
                music = { Text("Music") }
            )
        }
    }
}
