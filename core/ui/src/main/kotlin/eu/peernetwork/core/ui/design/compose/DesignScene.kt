package eu.peernetwork.core.ui.design.compose

import android.content.res.Configuration
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import eu.peernetwork.core.ui.theme.PeerTheme

sealed interface DesignSceneState<out T> {
    data object Default : DesignSceneState<Nothing>
    data object Loading : DesignSceneState<Nothing>
    data class Success<T>(val data: T) : DesignSceneState<T>
    data class Error(val error: Throwable) : DesignSceneState<Nothing>
}

@Composable
@Suppress("UNCHECKED_CAST")
fun<T> DesignScene(
    state: State<DesignSceneState<T>>,
    modifier: Modifier = Modifier,
    animationSpec: FiniteAnimationSpec<Float> = tween(),
    label: String = "Crossfade",
    durationMillis: Int = 1000,
    easing: Easing = FastOutSlowInEasing,
    default: @Composable () -> Unit = {},
    loading: @Composable () -> Unit = {},
    error: @Composable (error: State<Throwable>) -> Unit = {},
    content: @Composable (data: State<T>) -> Unit,
) {
    val infiniteTransition = rememberInfiniteTransition()
    val alpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis, easing = easing),
            repeatMode = RepeatMode.Reverse
        )
    )
    val updatedDefault by rememberUpdatedState(default)
    val updatedLoading by rememberUpdatedState(loading)
    val updatedError by rememberUpdatedState(error)
    val updatedContent by rememberUpdatedState(content)
    Crossfade(
        targetState = state.value,
        modifier = modifier,
        animationSpec = animationSpec,
        label = label
    ) { target ->
        when (target) {
            is DesignSceneState.Default -> updatedDefault()
            is DesignSceneState.Loading -> Box(modifier = Modifier.graphicsLayer {
                this.alpha = alpha
            }) { updatedLoading() }
            is DesignSceneState.Success<*> -> {
                val data = remember { derivedStateOf { (target as DesignSceneState.Success).data } }
                updatedContent(data)
            }
            is DesignSceneState.Error -> {
                val exception = remember { derivedStateOf { target.error } }
                updatedError(exception)
            }
        }
    }
}

@Composable
@Suppress("UNCHECKED_CAST")
fun<T> DesignScene(
    state: State<T>,
    modifier: Modifier = Modifier,
    isLoading: State<Boolean>,
    exception: State<Throwable?>,
    animationSpec: FiniteAnimationSpec<Float> = tween(),
    label: String = "Crossfade",
    durationMillis: Int = 1000,
    easing: Easing = FastOutSlowInEasing,
    default: @Composable () -> Unit = {},
    loading: @Composable () -> Unit = {},
    error: @Composable (error: State<Throwable?>) -> Unit = {},
    content: @Composable (data: State<T>) -> Unit,
) {
    val infiniteTransition = rememberInfiniteTransition()
    val alpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis, easing = easing),
            repeatMode = RepeatMode.Reverse
        )
    )
    val derivedState = remember { derivedStateOf {
        if (isLoading.value) {
            DesignSceneState.Loading
        } else if (exception.value != null) {
            DesignSceneState.Error(exception.value!!)
        } else {
            DesignSceneState.Success(state.value)
        }
    } }
    val updatedDefault by rememberUpdatedState(default)
    val updatedLoading by rememberUpdatedState(loading)
    val updatedError by rememberUpdatedState(error)
    val updatedContent by rememberUpdatedState(content)
    Crossfade(
        targetState = derivedState.value,
        modifier = modifier,
        animationSpec = animationSpec,
        label = label
    ) { target ->
        when (target) {
            is DesignSceneState.Default -> updatedDefault()
            is DesignSceneState.Loading -> Box(modifier = Modifier.graphicsLayer {
                this.alpha = alpha
            }) { updatedLoading() }
            is DesignSceneState.Success<*> -> updatedContent(state)
            is DesignSceneState.Error -> updatedError(exception)
        }
    }
}

@Composable
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
fun DesignScenePreview() {
    PeerTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            @Composable
            fun <T> PreviewBox(state: DesignSceneState<T>) {
                val rememberedState = remember { mutableStateOf(state) }
                CompositionLocalProvider(LocalTextStyle provides MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onPrimary
                )) {
                    DesignScene(
                        state = rememberedState,
                        default = { Text("Default State") },
                        loading = { Text("Loading...") },
                        error = { e -> Text("Error: ${e.value.message}") },
                        content = { data -> Text("Success: ${data.value}") }
                    )
                }
            }
            PreviewBox(DesignSceneState.Default)
            PreviewBox(DesignSceneState.Loading)
            PreviewBox(DesignSceneState.Success("Hello Compose"))
            PreviewBox(DesignSceneState.Error(Throwable("Something went wrong")))
        }
    }
}
