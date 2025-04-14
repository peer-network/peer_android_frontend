package eu.peernetwork.core.common.provider

import kotlinx.coroutines.CoroutineDispatcher

interface Dispatcher {
    val io: CoroutineDispatcher
    val main: CoroutineDispatcher
    val default: CoroutineDispatcher

    interface Provider {
        fun dispatcher(): Dispatcher
    }
}
