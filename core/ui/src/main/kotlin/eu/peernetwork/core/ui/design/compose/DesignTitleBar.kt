package eu.peernetwork.core.ui.design.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

interface DesignTitleBarBuilder {
    fun titleBar(content: @Composable () -> Unit)
}

interface DesignTitleBarRegistry {
    fun attach(
        tag: String,
        listener: () -> Unit,
        content: @Composable () -> Unit
    )

    fun current(): DesignTitleBar?

    fun detach()
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
    val elements = remember { mutableStateMapOf<Int, DesignTitleBar>() }
    val updatedContent by rememberUpdatedState(content)
    val builders = remember {
        object : DesignTitleBarRegistry {
            override fun attach(
                tag: String,
                listener: () -> Unit,
                content: @Composable () -> Unit
            ) {
                elements[elements.keys.size] = DesignTitleBar(tag, listener, content)
            }

            override fun current(): DesignTitleBar? = elements.keys.lastOrNull()?.let {
                elements[it]
            }

            override fun detach() {
                elements.keys.lastOrNull()?.let {
                    elements.remove(it)
                }
            }
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
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .padding(horizontal = 8.dp),
    ) { updatedContent() }
}

@Composable
fun DesignTitleBarHost(
    tag: String,
    listener: () -> Unit = {},
    builder: DesignTitleBarBuilder.() -> Unit,
) {
    val toolbar = rememberDesignToolbar()
    val updatedBuilder by rememberUpdatedState(builder)
    val titleBarBuilder = remember {
        object : DesignTitleBarBuilder {
            override fun titleBar(
                content: @Composable (() -> Unit)
            ) { toolbar.attach(tag, listener, content) }
        }
    }
    LaunchedEffect(Unit) {
        updatedBuilder(titleBarBuilder)
    }
    DisposableEffect(Unit) {
        onDispose {
            if (toolbar.current()?.tag == tag) {
                toolbar.detach()
            }
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
