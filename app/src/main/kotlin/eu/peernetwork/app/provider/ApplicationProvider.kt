package eu.peernetwork.app.provider

import android.content.Context
import eu.peernetwork.core.common.provider.CoreProvider
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.gson.Gson
import eu.peernetwork.ads.ui.provider.AdsProvider
import eu.peernetwork.app.interactor.RemoteInteractor
import eu.peernetwork.app.interactor.SettingsInteractor
import eu.peernetwork.app.interceptor.SubscriptionInteractor
import eu.peernetwork.app.service.BootstrapService
import eu.peernetwork.app.service.NetworkService
import eu.peernetwork.core.remote.api.RequestClient
import eu.peernetwork.persistence.domain.provider.PreferenceProvider
import eu.peernetwork.user.ui.provider.UserProvider
import eu.peernetwork.wallet.ui.provider.WalletProvider

interface ApplicationProvider : UserProvider,
    PreferenceProvider,
    ContentProvider,
    WalletProvider,
    CoreProvider,
    AdsProvider {
    fun context(): Context

    fun gson(): Gson

    fun firebaseRemoteConfig(): FirebaseRemoteConfig

    fun bootstrapService(): BootstrapService

    fun networkProvider(): RequestClient

    fun networkService(): NetworkService

    fun remoteInteractor(): RemoteInteractor
    
    fun settingsInteractor(): SettingsInteractor

    fun subscriptionInteractor(): SubscriptionInteractor
}
