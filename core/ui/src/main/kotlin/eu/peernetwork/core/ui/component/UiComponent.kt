package eu.peernetwork.core.ui.component

import android.content.Context

interface UiComponent {
    interface Provider<T> {
        val injector: T
    }

    interface Builder

    abstract class DefaultBuilder<P, C : P> : Builder {
        abstract fun build(context: Context): C
    }

    abstract class ParameterizedBuilder<A, P, C : P> : Builder {
        abstract fun build(context: Context, param: A): C
    }
}
