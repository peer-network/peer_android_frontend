package eu.peernetwork.blog.ui.compose

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import eu.peernetwork.core.common.interactor.ResourceInteractor
import eu.peernetwork.core.ui.design.component.DesignError
import eu.peernetwork.core.ui.design.compose.DesignPager
import eu.peernetwork.core.ui.design.compose.DesignPagerState
import eu.peernetwork.core.ui.design.compose.DesignRefreshablePager
import eu.peernetwork.core.ui.design.compose.DesignSceneState
import eu.peernetwork.core.ui.theme.PeerTheme
import kotlinx.coroutines.flow.Flow

@Composable
fun<T : Any> ContentScaffold(
    state: State<DesignSceneState<Flow<PagingData<T>>>>,
    modifier: Modifier = Modifier,
    resource: ResourceInteractor,
    onRefresh: () -> Unit = {},
    default: @Composable () -> Unit = {
        Box(modifier = Modifier.fillMaxSize()
            .verticalScroll(rememberScrollState())
        ) { PostPlaceholder(contentPaddingValues = PaddingValues(16.dp)) }
    },
    loading: @Composable () -> Unit = {
        Box(modifier = Modifier.fillMaxSize()
            .verticalScroll(rememberScrollState())
        ) { PostPlaceholder(contentPaddingValues = PaddingValues(16.dp)) }
    },
    content: @Composable (State<DesignPagerState>, data: State<LazyPagingItems<T>>) -> Unit
) {
    DesignPager(
        state = state,
        modifier = modifier,
        default = default,
        loading = loading,
        error = { DesignError(onRefresh, it.value, resource) },
        content = content
    )
}

@Composable
fun<T : Any> RefreshableContentScaffold(
    state: State<DesignSceneState<Flow<PagingData<T>>>>,
    modifier: Modifier = Modifier,
    resource: ResourceInteractor,
    onRefresh: () -> Unit = {},
    default: @Composable () -> Unit = {
        Box(modifier = Modifier.fillMaxSize()
            .verticalScroll(rememberScrollState())
        ) { PostPlaceholder(contentPaddingValues = PaddingValues(16.dp)) }
    },
    loading: @Composable () -> Unit = {
        Box(modifier = Modifier.fillMaxSize()
            .verticalScroll(rememberScrollState())
        ) { PostPlaceholder(contentPaddingValues = PaddingValues(16.dp)) }
    },
    content: @Composable (State<DesignPagerState>, data: State<LazyPagingItems<T>>) -> Unit
) {
    DesignRefreshablePager(
        state = state,
        modifier = modifier,
        onRefresh = onRefresh,
        default = default,
        loading = loading,
        error = { DesignError(onRefresh, it.value, resource) },
        content = content
    )
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Default Screen")
fun ContentScaffoldPreview() {
    PeerTheme {
        PreviewBox<Nothing>(
            state = DesignSceneState.Default,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        )
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Loading Screen")
fun ContentScaffoldLoadingPreview() {
    PeerTheme {
        PreviewBox<Nothing>(
            state = DesignSceneState.Loading,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        )
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Error Screen")
fun ContentScaffoldErrorPreview() {
    PeerTheme {
        PreviewBox<Nothing>(
            state = DesignSceneState.Error(Throwable("Something went wrong")),
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp)
        )
    }
}

@Composable
private fun <T : Any> PreviewBox(
    state: DesignSceneState<Flow<PagingData<T>>>,
    modifier: Modifier = Modifier
) {
    val rememberedState = remember { mutableStateOf(state) }
    val resource = object : ResourceInteractor {
        override fun getBaseUrl(): String = "http://localhost"

        override fun string(key: String): String = key
    }
    CompositionLocalProvider(
        LocalTextStyle provides MaterialTheme.typography.bodyMedium.copy(
            color = MaterialTheme.colorScheme.onPrimary
        )
    ) {
        RefreshableContentScaffold(
            state = rememberedState,
            modifier = modifier,
            resource = resource,
            content = { state, data -> Text("Success: ${data.value}") },
        )
    }
}
