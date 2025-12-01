package eu.peernetwork.social.ui.connection

import androidx.compose.runtime.staticCompositionLocalOf
import kotlinx.coroutines.flow.StateFlow

interface ConnectionInteractor {
    operator fun invoke(id: String, value: Boolean)

    fun observe(): StateFlow<Map<String, Boolean>>

    companion object {
        val LocalConnectionInteractor = staticCompositionLocalOf<ConnectionInteractor> {
            error("ConnectionInteractor not provided")
        }
    }
}
