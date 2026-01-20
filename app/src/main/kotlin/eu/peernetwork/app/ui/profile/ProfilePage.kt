package eu.peernetwork.app.ui.profile

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableLongState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavHostController
import eu.peernetwork.blog.domain.usecase.PostUsecase
import eu.peernetwork.blog.ui.article.ArticleEvent
import eu.peernetwork.blog.ui.article.ArticleList
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.design.material.DesignTitle
import eu.peernetwork.core.ui.design.material.DesignTitleBarHost
import eu.peernetwork.core.ui.extension.navigateIfNecessary
import eu.peernetwork.social.ui.connection.ConnectionStatus
import eu.peernetwork.social.ui.report.ReportSheet
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ProfilePage(
    uuid: String,
    user: String,
    username: String,
    imageUrl: String,
    title: String?,
    limit: Int,
    pageState: PagerState,
    isVisible: State<Boolean>,
    selected: MutableIntState,
    timestamp: MutableLongState,
    postState: LazyListState = rememberLazyListState(),
    mediaState: LazyListState = rememberLazyListState(),
    component: Profile.Component,
    viewModelStoreOwner: ViewModelStoreOwner,
    onSettings: () -> Unit,
    onBoost: (ArticleEvent.Boost) -> Unit,
    onClick: () -> Unit,
    controller: NavHostController,
) {
    val scope = rememberCoroutineScope()
    val requireUpdate = rememberSaveable { mutableStateOf(false) }
    val requirePostUpdate = rememberSaveable { mutableStateOf(false) }
    val report = remember { mutableStateOf<String?>(null) }
    val connection = remember { mutableStateOf<ConnectionStatus?>(null) }
    val handleClick by rememberUpdatedState(onClick)
    val handleBoost by rememberUpdatedState(onBoost)
    ProfileScaffold(
        pageState = pageState,
        onRefresh = {
            timestamp.longValue = System.currentTimeMillis()
            requireUpdate.value = true
            requirePostUpdate.value = true
        },
        header = {
            ProfileDetail(
                id = user,
                connection = connection,
                timestamp = timestamp,
                onSettings = onSettings,
                onBlock = { report.value = user },
                onMenuClicked = { controller.navigate("adverts") },
                component = component,
                viewModelStoreOwner = viewModelStoreOwner
            )
        },
    ) {
        ArticleList(
            uuid = uuid,
            author = user,
            username = username,
            imageUrl = imageUrl,
            types = if (it == 0) {
                PostUsecase.POST
            } else {
                PostUsecase.MEDIA
            },
            status = isVisible,
            limit = limit,
            selected = selected,
            timestamp = timestamp,
            provider = component,
            viewModelStoreOwner = viewModelStoreOwner,
            onEvent = { event ->
                when(event) {
                    is ArticleEvent.Boost -> handleBoost(event)
                    is ArticleEvent.Post -> handleClick()
                }
            },
            listState =  if (it == 0) {
                postState
            } else {
                mediaState
            }
        )
    }
    ReportSheet(
        state = report,
        provider = component,
        viewModelStoreOwner = viewModelStoreOwner
    )
    ProfileSheet(
        id = user,
        state = connection,
        limit = limit,
        provider = component,
        viewModelStoreOwner = viewModelStoreOwner
    ) { controller.navigateIfNecessary("profile/$it") }
    DesignTitleBarHost("ProfileScreen$user", {
        scope.launch {
            if (pageState.currentPage == 0) {
                postState.animateScrollToItem(0)
            } else {
                mediaState.animateScrollToItem(0)
            }
        }
    }) {
        titleBar {
            DesignTitle {
                Text(title ?: stringResource(R.string.profile_label))
            }
        }
    }
}
