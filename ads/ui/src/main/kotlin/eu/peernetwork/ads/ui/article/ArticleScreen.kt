package eu.peernetwork.ads.ui.article

import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import eu.peernetwork.ads.domain.model.Content
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.component.UiComponentProvider
import eu.peernetwork.core.ui.design.luna.DesignStream
import eu.peernetwork.core.ui.design.luna.DesignStreamState
import eu.peernetwork.core.ui.design.material.DesignScaffold
import eu.peernetwork.core.ui.extension.annotate
import eu.peernetwork.core.ui.extension.builder

@Composable
fun ArticleScreen(
    id: String,
    provider: UiComponentProvider,
    viewModelStoreOwner: ViewModelStoreOwner,
    content: @Composable (Content) -> Unit
) {
    val context = LocalContext.current
    val component = remember { provider.builder(Article.Builder::class.java).build(context) }
    val viewModel = viewModel(
        modelClass = ArticleViewModel::class.java,
        viewModelStoreOwner = viewModelStoreOwner,
        factory = component.viewModelFactory()
    )
    val errorMessage = stringResource(R.string.unknown_error_message)
    val state by viewModel.states.collectAsStateWithLifecycle()
    val updatedContent by rememberUpdatedState(content)
    val localState = remember { derivedStateOf {
        state[id] ?: ArticleViewModel.State.Default
    } }
    val derivedState = remember {
        derivedStateOf {
            val currentState = localState.value
            when (currentState) {
                is ArticleViewModel.State.Default -> DesignStreamState.Default
                is ArticleViewModel.State.Loading -> DesignStreamState.Loading
                is ArticleViewModel.State.Success -> DesignStreamState.Success(
                    currentState.content
                )
                is ArticleViewModel.State.Error -> DesignStreamState.Error(
                    currentState.error.let {
                        Throwable(component.resource()
                            .string(it.message ?: errorMessage), it)
                    }
                )
            }
        }
    }
    DesignScaffold {
        DesignStream(
            state = derivedState,
            loading = { ArticleSkeleton() }
        ) { targetState ->
            DesignScaffold(
                modifier = Modifier.statusBarsPadding(),
                header = {
                    ArticlePage(
                        title = targetState.value.title.annotate(),
                        description = targetState.value.description.annotate(),
                    ) { ArticleMedia(targetState.value.path, component) }
                }
            ) { updatedContent(targetState.value) }
        }
    }
    LaunchedEffect(Unit) {
        if (localState.value is ArticleViewModel.State.Default) {
            viewModel(id)
        }
    }
}
