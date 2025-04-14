package eu.peernetwork.core.ui.design.component

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.materii.pullrefresh.DragRefreshLayout
import dev.materii.pullrefresh.rememberPullRefreshState
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
@Suppress("UNCHECKED_CAST")
fun<T> DesignRefreshableContent(
    state: State<DesignStatefulContentState>,
    modifier: Modifier = Modifier,
    onRefresh: () -> Unit = {},
    placeholder: (@Composable () -> Unit)? = null,
    errorContent: (@Composable (Throwable) -> Unit)? = null,
    content: @Composable (T) -> Unit,
) {
    val contentState = remember { mutableStateOf<T?>(null) }
    val errorState = remember { mutableStateOf<Throwable?>(null) }
    val refreshState = rememberPullRefreshState(
        refreshing = state.value is DesignStatefulContentState.Loading,
        onRefresh = onRefresh
    )
    DragRefreshLayout(state = refreshState, modifier = modifier) {
        if (errorState.value != null) {
            errorContent?.invoke(errorState.value!!)
                ?: DesignRefreshErrorContent(
                    errorState.value!!,
                    modifier = Modifier.fillMaxSize()
                        .verticalScroll(rememberScrollState())
                )
        } else if (contentState.value != null) {
            content(contentState.value!!)
        } else if (state.value is DesignStatefulContentState.Empty) {
            placeholder?.invoke() ?: DesignRefreshErrorContent(
                RuntimeException(stringResource(R.string.empty_message)),
                modifier = Modifier.fillMaxSize()
                    .verticalScroll(rememberScrollState())
            )
        }
    }
    LaunchedEffect(state.value) {
        when (state.value) {
            DesignStatefulContentState.Empty -> {}
            DesignStatefulContentState.Loading -> {}
            is DesignStatefulContentState.Success<*> -> {
                contentState.value = (state.value as DesignStatefulContentState.Success<*>).result as T
            }
            is DesignStatefulContentState.Error -> {
                errorState.value = (state.value as DesignStatefulContentState.Error).error
            }
        }
    }
}

@Composable
fun DesignRefreshErrorContent(
    error: Throwable,
    modifier: Modifier = Modifier,
    textAlign: TextAlign? = null,
    verticalArrangement: Arrangement.Vertical = Arrangement.Center,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    style: TextStyle = MaterialTheme.typography.bodyMedium.copy(
        color = MaterialTheme.colorScheme.tertiary
    )
) {
    Column(
        modifier = modifier,
        verticalArrangement = verticalArrangement,
        horizontalAlignment = horizontalAlignment
    ) {
        Text(
            if (!error.message.isNullOrEmpty()) {
                error.message!!
            } else {
                stringResource(R.string.unknown_error_message)
            },
            textAlign = textAlign,
            style = style
        )
        Text(
            stringResource(R.string.refresh_message),
            textAlign = textAlign,
            modifier = Modifier.padding(top = 4.dp),
            style = style.copy(
                fontSize = MaterialTheme.typography.bodySmall.fontSize
            )
        )
        Spacer(modifier = Modifier.height(64.dp))
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewDesignRefreshableContent() {
    PeerTheme {
        DesignRefreshErrorContent(
            RuntimeException("Hello, world!"),
            Modifier.fillMaxSize()
        )
    }
}
