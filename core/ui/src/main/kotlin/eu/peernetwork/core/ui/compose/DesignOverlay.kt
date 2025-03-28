package eu.peernetwork.core.ui.compose

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import eu.peernetwork.core.ui.theme.PeerTheme

interface DesignOverlayBuilder {
    fun overlay(zIndex: Float = 1f, content: @Composable () -> Unit)
}

interface DesignOverlayController {
    fun show(tag: String)

    fun isVisible(tag: String): Boolean

    fun dismiss(tag: String)
}

private interface DesignOverlayRegistry {
    fun get(tag: String): @Composable (() -> Unit)?

    fun register(tag: String, zIndex: Float, show: Boolean, content: @Composable () -> Unit)
}

@Composable
fun DesignOverlay(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    val routes = remember { linkedSetOf<String?>(null).toMutableStateList() }
    val registry = remember { mutableStateMapOf<String, Pair<Float, @Composable () -> Unit>>() }
    val currentController = remember(routes) {
        object : DesignOverlayController {
            override fun show(tag: String) {
                if (!routes.contains(tag)) {
                    routes.add(tag)
                }
            }

            override fun isVisible(tag: String): Boolean {
                return routes.contains(tag)
            }

            override fun dismiss(tag: String) {
                routes.remove(tag)
            }
        }
    }
    val overlayRegistry = remember {
        object : DesignOverlayRegistry {
            override fun register(
                tag: String,
                zIndex: Float,
                show: Boolean,
                content: @Composable (() -> Unit)
            ) {
                require(!registry.containsKey(tag)) { "Tag $tag already registered" }
                registry[tag] = zIndex to content
                if (show) {
                    routes.add(tag)
                }
            }

            override fun get(tag: String): @Composable (() -> Unit)? {
                return registry[tag]?.second
            }
        }
    }
    CompositionLocalProvider(
        LocalDesignOverlayRegistry provides overlayRegistry,
        LocalDesignOverlayController provides currentController,
    ) {
        Box(modifier) {
            content()
            Crossfade(targetState = routes.lastOrNull()) { tag ->
                tag?.let {
                    key(it) {
                        Box(Modifier.zIndex(registry[it]?.first ?: 1f)) {
                            registry[it]?.second?.invoke()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DesignOverlayHost(
    tag: String,
    show: Boolean = false,
    builder: DesignOverlayBuilder.() -> Unit
) {
    val overlayRegistry = LocalDesignOverlayRegistry.current
    val overlayBuilder = remember {
        object : DesignOverlayBuilder {
            override fun overlay(zIndex: Float, content: @Composable () -> Unit) {
                overlayRegistry.register(tag, zIndex, show, content)
            }
        }
    }
    builder(overlayBuilder)
}

val LocalDesignOverlayController = staticCompositionLocalOf<DesignOverlayController> {
    error("No DesignOverlayController provided")
}

private val LocalDesignOverlayRegistry = staticCompositionLocalOf<DesignOverlayRegistry> {
    error("No DesignOverlayRegistry provided")
}

@Composable
fun rememberDesignOverlayController(): DesignOverlayController {
    return LocalDesignOverlayController.current
}

@Composable
@Preview
fun PreviewDesignOverlay() {
    PeerTheme {
        DesignOverlay(modifier = Modifier.fillMaxSize()) {
            val tag = "peer"
            val controller = rememberDesignOverlayController()
            Column {
                Button(onClick = {
                    if (controller.isVisible(tag)) {
                        controller.dismiss(tag)
                    } else {
                        controller.show(tag)
                    }
                }) { Text("toggle overlay") }
                DesignOverlayHost(tag, false) {
                    overlay {
                        Text("Hello, world!",
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.fillMaxSize()
                                .padding(24.dp)
                                .padding(top = 150.dp)
                        )
                    }
                }
            }
        }
    }
}
