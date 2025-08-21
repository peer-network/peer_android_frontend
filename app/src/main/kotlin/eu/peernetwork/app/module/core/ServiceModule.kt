package eu.peernetwork.app.module.core

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import dagger.Module
import dagger.Provides
import eu.peernetwork.app.interceptor.NotificationInteractorDelegate
import eu.peernetwork.app.interceptor.SubscriptionInteractor
import eu.peernetwork.app.service.BootstrapService
import eu.peernetwork.app.service.ResourceInteractorDelegate
import eu.peernetwork.app.service.SubscriptionService
import eu.peernetwork.core.common.interactor.NotificationInteractor
import eu.peernetwork.core.common.interactor.ResourceInteractor
import javax.inject.Singleton

@Module
object ServiceModule {
    @Provides
    fun resourceService(delegate: ResourceInteractorDelegate): ResourceInteractor = delegate

    @Provides
    fun resourceLoader(delegate: ResourceInteractor): BootstrapService {
        return delegate as BootstrapService
    }

    @Provides
    fun provideRemoteConfig(): FirebaseRemoteConfig = FirebaseRemoteConfig.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance().apply {
            firestoreSettings = FirebaseFirestoreSettings.Builder().build()
        }
    }

    @Provides
    @Singleton
    fun provideFirebaseFunctions(): FirebaseFunctions {
        return FirebaseFunctions.getInstance()
    }

    @Provides
    fun provideSubscriptionInteractor(delegate: SubscriptionService): SubscriptionInteractor = delegate

    @Provides
    fun providesNotificationInteractor(delegate: NotificationInteractorDelegate): NotificationInteractor = delegate
}
