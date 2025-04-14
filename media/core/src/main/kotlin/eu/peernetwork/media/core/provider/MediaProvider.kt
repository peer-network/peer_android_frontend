package eu.peernetwork.media.core.provider

interface MediaProvider : CoreProvider, RendererProvider {
    fun videoProvider(): VideoProvider
}
