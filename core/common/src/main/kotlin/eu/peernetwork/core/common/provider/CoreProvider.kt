package eu.peernetwork.core.common.provider

import eu.peernetwork.core.common.interactor.NotificationInteractor

interface CoreProvider : Dispatcher.Provider, ServiceProvider {
    fun notificationInteractor(): NotificationInteractor
}
