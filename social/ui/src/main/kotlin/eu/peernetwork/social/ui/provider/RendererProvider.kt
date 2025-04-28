package eu.peernetwork.social.ui.provider

import eu.peernetwork.social.ui.renderder.BlogRenderer
import eu.peernetwork.social.ui.renderder.UserRenderer

interface RendererProvider {
    fun blogRenderer(): BlogRenderer

    fun userRenderer(): UserRenderer
}
