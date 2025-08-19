package eu.peernetwork.core.common.provider

import eu.peernetwork.core.common.interactor.NotificationInteractor
import eu.peernetwork.core.common.interactor.UrlInteractor

interface CoreProvider : Dispatcher.Provider, ServiceProvider {
    fun urlInteractor(): UrlInteractor

    fun notificationInteractor(): NotificationInteractor
}
