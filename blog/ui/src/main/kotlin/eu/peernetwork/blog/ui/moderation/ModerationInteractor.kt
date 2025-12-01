package eu.peernetwork.blog.ui.moderation

import androidx.compose.runtime.staticCompositionLocalOf

interface ModerationInteractor {
    fun onReport(id: String)

    companion object {
        val LocalModerationInteractor = staticCompositionLocalOf<ModerationInteractor> {
            error("ModerationInteractor not provided")
        }
    }
}
