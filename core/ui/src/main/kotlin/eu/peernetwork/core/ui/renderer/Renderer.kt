package eu.peernetwork.core.ui.renderer

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

interface Renderer {
    interface Stateless : Renderer {
        @Composable
        operator fun invoke(modifier: Modifier)
    }
    interface Stateful<T> : Renderer {
        @Composable
        operator fun invoke(modifier: Modifier, spec: T)
    }
}