package eu.peernetwork.core.ui.design.component

import android.content.res.Configuration
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
@Suppress("UNCHECKED_CAST")
fun<T> DesignStatefulScaffold(
    state: State<DesignStatefulScaffoldState>,
    modifier: Modifier = Modifier,
    autoRefresh: Boolean = true,
    onRefresh: () -> Unit = {},
    durationMillis: Int = 1000,
    easing: Easing = FastOutSlowInEasing,
    contentAlignment: Alignment = Alignment.Center,
    placeholder: (@Composable () -> Unit)? = null,
    errorContent: (@Composable (Throwable) -> Unit)? = null,
    content: @Composable (T) -> Unit,
) {
    val isEmpty = remember { derivedStateOf { state.value is DesignStatefulScaffoldState.Empty } }
    val infiniteTransition = rememberInfiniteTransition()
    val alpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis, easing = easing),
            repeatMode = RepeatMode.Reverse
        )
    )
    val handleRefresh by rememberUpdatedState(onRefresh)
    val updatedContent by rememberUpdatedState(content)
    val updatedPlaceholder by rememberUpdatedState(placeholder)
    val updatedErrorContent by rememberUpdatedState(errorContent)
    Box(
        modifier = modifier.graphicsLayer {
            if (state.value is DesignStatefulScaffoldState.Loading) {
                this.alpha = alpha
            } else {
                1
            }
        },
        contentAlignment = contentAlignment
    ) {
        when (state.value) {
            DesignStatefulScaffoldState.Empty -> {
                updatedPlaceholder?.invoke() ?: DesignStatefulContentPlaceholder()
            }
            DesignStatefulScaffoldState.Loading -> {
                updatedPlaceholder?.invoke() ?: DesignStatefulContentPlaceholder(
                        text = stringResource(R.string.loading_text))
            }
            is DesignStatefulScaffoldState.Success<*> -> {
                updatedContent((state.value as DesignStatefulScaffoldState.Success<*>).result as T)
            }
            is DesignStatefulScaffoldState.Error -> {
                val error = (state.value as DesignStatefulScaffoldState.Error)
                updatedErrorContent?.invoke(error.error)
                    ?: DesignError(error.error, onRetry = onRefresh)
            }
        }
    }
    LaunchedEffect(isEmpty.value) {
        if (isEmpty.value && autoRefresh) {
            handleRefresh()
        }
    }
}

@Composable
fun DesignStatefulContentPlaceholder(
    modifier: Modifier = Modifier,
    text: String? = null,
    contentAlignment: Alignment = Alignment.Center,
) {
    Box(
        modifier = modifier,
        contentAlignment = contentAlignment
    ) { text?.let {
        Text(it, style = MaterialTheme.typography.bodySmall)
    } }
}

sealed interface DesignStatefulScaffoldState {
    data object Empty : DesignStatefulScaffoldState
    data object Loading : DesignStatefulScaffoldState
    data class Success<T>(val result: T) : DesignStatefulScaffoldState
    data class Error(val error: Throwable) : DesignStatefulScaffoldState
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewDesignStatefulContent() {
    PeerTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            DesignStatefulScaffold<String>(
                state = remember { mutableStateOf(DesignStatefulScaffoldState.Empty) },
                modifier = Modifier.fillMaxWidth().weight(1f),
                errorContent = { Text("${it.message}") }
            ) {}
            DesignStatefulScaffold<String>(
                state = remember { mutableStateOf(DesignStatefulScaffoldState.Loading) },
                modifier = Modifier.fillMaxWidth().weight(1f),
            ) {}
            DesignStatefulScaffold<String>(
                state = remember { mutableStateOf(DesignStatefulScaffoldState.Success("Content")) },
                modifier = Modifier.fillMaxWidth().weight(1f),
            ) { Text(it) }
        }
    }
}
