package eu.peernetwork.core.common.provider

import eu.peernetwork.core.common.service.ResourceService

interface ServiceProvider {
    fun resource(): ResourceService
}
