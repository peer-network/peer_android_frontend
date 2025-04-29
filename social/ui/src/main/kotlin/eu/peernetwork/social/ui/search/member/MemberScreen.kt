package eu.peernetwork.social.ui.search.member

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.component.DesignPagingScaffold
import eu.peernetwork.core.ui.design.component.DesignStatefulScaffoldState
import eu.peernetwork.core.ui.extension.builder
import eu.peernetwork.social.ui.model.UiMember

@Composable
fun MemberScreen(
    query: TextFieldState,
    postLimit: Int,
    onClick: (String) -> Unit,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
) {
    val context = LocalContext.current
    val component = remember {
        provider.builder(Member.Builder::class.java).build(context)
    }
    val viewModel = viewModel(
        modelClass = MemberViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val state by viewModel.state.collectAsStateWithLifecycle()
    val derivedState = remember {
        derivedStateOf {
            when (state) {
                MemberViewModel.State.Empty -> DesignStatefulScaffoldState.Empty
                MemberViewModel.State.Loading -> DesignStatefulScaffoldState.Loading
                is MemberViewModel.State.Success -> {
                    DesignStatefulScaffoldState.Success(
                        (state as MemberViewModel.State.Success).content
                    )
                }
                is MemberViewModel.State.Error -> DesignStatefulScaffoldState.Error(
                    (state as MemberViewModel.State.Error).error
                )
            }
        }
    }
    DesignPagingScaffold<UiMember>(
        state = derivedState,
        onRefresh = { viewModel.search(query.text.toString(), Pageable(0, postLimit)) },
        modifier = Modifier.padding(horizontal = 24.dp)
    ) { state, lazyPagingItems ->
        LazyColumn {
            items(
                count = lazyPagingItems.itemCount,
                key = { index -> index }
            ) { index ->
                lazyPagingItems[index]?.let { member ->
                    Box(modifier = Modifier.fillMaxWidth()
                        .padding(vertical = 16.dp)
                        .clickable(role = Role.Button) {
                        onClick(member.id)
                    }) {
                        Text(
                            text = "@${member.username}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.tertiary,
                        )
                    }
                }
            }
        }
    }
    LaunchedEffect(query.text) {
        if (query.text.length >= 3) {
            viewModel.search(query.text.toString(), Pageable(0, postLimit))
        }
    }
}
