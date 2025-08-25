package eu.peernetwork.core.common.provider

import eu.peernetwork.core.common.interactor.ResourceInteractor

interface ServiceProvider {
    fun resource(): ResourceInteractor
}
