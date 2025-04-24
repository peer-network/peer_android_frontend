package eu.peernetwork.core.ui.design.component

import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun<T : Any> DesignScreenScaffold(
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
    DesignStatefulScaffold(
        state = state,
        modifier = modifier,
        autoRefresh = autoRefresh,
        onRefresh = onRefresh,
        durationMillis = durationMillis,
        easing = easing,
        contentAlignment = contentAlignment,
        placeholder = placeholder ?: {
            DesignStatefulContentPlaceholder(modifier = Modifier.fillMaxSize()
                .verticalScroll(rememberScrollState()))
        },
        errorContent = errorContent ?: {
            DesignErrorContent(it, onRetry = onRefresh, modifier = Modifier.fillMaxSize()
                .verticalScroll(rememberScrollState()))
        },
        content = content
    )
}
