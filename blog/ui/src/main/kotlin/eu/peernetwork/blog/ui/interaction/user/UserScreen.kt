package eu.peernetwork.blog.ui.interaction.user

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.LazyPagingItems
import eu.peernetwork.blog.domain.model.Engagement
import eu.peernetwork.blog.ui.model.UiAuthor
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.luna.DesignPagingStream
import eu.peernetwork.core.ui.design.luna.DesignStreamState
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.core.ui.extension.error

@Composable
fun UserScreen(
    id: String,
    limit: Int,
    engagement: Engagement.Content,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    content: @Composable (User.Component, LazyPagingItems<UiAuthor>) -> Unit
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(User.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = UserViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val currentId = remember { derivedStateOf {
        val currentState = state["$id/$engagement"] ?: UserViewModel.State.Empty
        (currentState as? UserViewModel.State.Success?)?.id
    } }
    val derivedState = remember {
        derivedStateOf {
            val currentState = state["$id/$engagement"] ?: UserViewModel.State.Empty
            when (currentState) {
                UserViewModel.State.Empty -> DesignStreamState.Default
                UserViewModel.State.Loading -> DesignStreamState.Loading
                is UserViewModel.State.Success -> DesignStreamState.Success(
                    currentState.content
                )
                is UserViewModel.State.Error -> DesignStreamState.Error(
                    currentState.error
                )
            }
        }
    }
    val updatedContent by rememberUpdatedState(content)
    DesignPagingStream(
        state = derivedState,
        loading = { UserSkeleton(4) },
        error = {
            UserError(component.resource().error(it.value)) {
                viewModel.load(
                    id,
                    engagement,
                    Pageable(offset = 0, limit = limit)
                )
            }
        }
    ) { updatedContent(component, it) }
    LaunchedEffect(Unit) {
        if (id != currentId.value) {
            viewModel.reset()
        }
        if (derivedState.value is DesignStreamState.Default) {
            viewModel.load(
                id,
                engagement,
                Pageable(offset = 0, limit = limit)
            )
        }
    }
}
