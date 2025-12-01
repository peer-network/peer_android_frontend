package eu.peernetwork.core.ui.design.material

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner

interface DesignTitleBarBuilder {
    fun titleBar(content: @Composable () -> Unit)
}

interface DesignTitleBarRegistry {
    fun attach(
        tag: String,
        listener: () -> Unit,
        content: @Composable () -> Unit
    )

    fun titleBar(): State<DesignTitleBar?>
}

@Immutable
data class DesignTitleBar(
    val tag: String,
    val listener: () -> Unit,
    val content: @Composable () -> Unit,
)

@Composable
fun DesignTitleBar(
    content: @Composable DesignTitleBarRegistry.() -> Unit
) {
    val mutableToolBar = remember { mutableStateOf<DesignTitleBar?>(null) }
    val updatedContent by rememberUpdatedState(content)
    val builders = remember {
        object : DesignTitleBarRegistry {
            override fun attach(
                tag: String,
                listener: () -> Unit,
                content: @Composable () -> Unit
            ) {
                mutableToolBar.value = DesignTitleBar(tag, listener, content)
            }

            override fun titleBar(): State<DesignTitleBar?> = mutableToolBar
        }
    }
    CompositionLocalProvider(LocalDesignTitleBarRegistry provides builders) {
        updatedContent(builders)
    }
}

@Composable
fun DesignTitle(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val updatedContent by rememberUpdatedState(content)
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(4.dp))
            .then(modifier)
            .padding(horizontal = 8.dp),
    ) {
        CompositionLocalProvider(LocalTextStyle provides MaterialTheme.typography.labelLarge) {
            updatedContent()
        }
    }
}

@Composable
fun DesignTitleBarHost(
    tag: String,
    listener: () -> Unit = {},
    builder: DesignTitleBarBuilder.() -> Unit,
) {
    val toolbar = rememberDesignToolbar()
    val lifecycleOwner = LocalLifecycleOwner.current
    val updatedBuilder by rememberUpdatedState(builder)
    val titleBarBuilder = remember {
        object : DesignTitleBarBuilder {
            override fun titleBar(
                content: @Composable (() -> Unit)
            ) { toolbar.attach(tag, listener, content) }
        }
    }
    val observer = remember {
        LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                updatedBuilder(titleBarBuilder)
            }
        }
    }
    LaunchedEffect(Unit) {
        lifecycleOwner.lifecycle.addObserver(observer)
    }
    DisposableEffect(Unit) {
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}

val LocalDesignTitleBarRegistry = staticCompositionLocalOf<DesignTitleBarRegistry> {
    error("No DesignTitleBarRegistry provided")
}

@Composable
fun rememberDesignToolbar(): DesignTitleBarRegistry {
    return LocalDesignTitleBarRegistry.current
}
