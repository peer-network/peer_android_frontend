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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import eu.peernetwork.core.ui.R
import eu.peernetwork.core.ui.theme.PeerTheme

@Composable
@Suppress("UNCHECKED_CAST")
fun<T> DesignStatefulContent(
    state: State<DesignStatefulContentState>,
    modifier: Modifier = Modifier,
    refresh: () -> Unit = {},
    durationMillis: Int = 1000,
    easing: Easing = FastOutSlowInEasing,
    contentAlignment: Alignment = Alignment.Center,
    placeholder: (@Composable () -> Unit)? = null,
    errorContent: (@Composable (Throwable) -> Unit)? = null,
    content: @Composable (T) -> Unit,
) {
    val isEmpty = remember { derivedStateOf { state.value is DesignStatefulContentState.Empty } }
    val infiniteTransition = rememberInfiniteTransition()
    val alpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis, easing = easing),
            repeatMode = RepeatMode.Reverse
        )
    )
    Box(
        modifier = modifier.then(
            if (state.value is DesignStatefulContentState.Loading) {
                Modifier.graphicsLayer { this.alpha = alpha }
            } else {
                Modifier
            }
        ),
        contentAlignment = contentAlignment
    ) {
        when (state.value) {
            DesignStatefulContentState.Empty -> {
                placeholder?.invoke() ?: DesignStatefulContentPlaceholder()
            }
            DesignStatefulContentState.Loading -> {
                placeholder?.invoke() ?: DesignStatefulContentPlaceholder(
                        text = stringResource(R.string.loading_text))
            }
            is DesignStatefulContentState.Success<*> -> {
                content((state.value as DesignStatefulContentState.Success<*>).result as T)
            }
            is DesignStatefulContentState.Error -> {
                val error = (state.value as DesignStatefulContentState.Error)
                errorContent?.invoke(error.error)
                    ?: DesignErrorContent(error.error, onRetry = refresh)
            }
        }
    }
    LaunchedEffect(isEmpty.value) {
        if (isEmpty.value) {
            refresh()
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

sealed interface DesignStatefulContentState {
    data object Empty : DesignStatefulContentState
    data object Loading : DesignStatefulContentState
    data class Success<T>(val result: T) : DesignStatefulContentState
    data class Error(val error: Throwable) : DesignStatefulContentState
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun PreviewDesignStatefulContent() {
    PeerTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            DesignStatefulContent<String>(
                state = remember { mutableStateOf(DesignStatefulContentState.Empty) },
                modifier = Modifier.fillMaxWidth().weight(1f),
                errorContent = { Text("${it.message}") }
            ) {}
            DesignStatefulContent<String>(
                state = remember { mutableStateOf(DesignStatefulContentState.Loading) },
                modifier = Modifier.fillMaxWidth().weight(1f),
            ) {}
            DesignStatefulContent<String>(
                state = remember { mutableStateOf(DesignStatefulContentState.Success("Content")) },
                modifier = Modifier.fillMaxWidth().weight(1f),
            ) { Text(it) }
        }
    }
}
