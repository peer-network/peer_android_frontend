package eu.peernetwork.core.ui.provider

import android.content.Context
import eu.peernetwork.core.common.provider.Dispatcher

interface UiProvider {
    fun context(): Context

    fun dispatcher(): Dispatcher
}
