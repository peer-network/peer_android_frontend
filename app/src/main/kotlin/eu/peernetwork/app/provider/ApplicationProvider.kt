package eu.peernetwork.app.provider

import android.content.Context
import eu.peernetwork.core.common.provider.CoreProvider
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import eu.peernetwork.app.interactor.SettingsInteractor
import eu.peernetwork.app.service.BootstrapService
import eu.peernetwork.app.service.NetworkService
import eu.peernetwork.core.remote.api.RequestClient
import eu.peernetwork.persistence.domain.provider.PreferenceProvider
import eu.peernetwork.user.ui.provider.UserProvider
import eu.peernetwork.wallet.ui.provider.WalletProvider

interface ApplicationProvider : UserProvider, PreferenceProvider, ContentProvider, WalletProvider, CoreProvider {
    fun context(): Context

    fun firebaseRemoteConfig(): FirebaseRemoteConfig

    fun bootstrapService(): BootstrapService

    fun networkProvider(): RequestClient

    fun networkResource(): NetworkService

    fun settingsInteractor(): SettingsInteractor
}
