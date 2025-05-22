package eu.peernetwork.app.provider

import android.content.Context
import eu.peernetwork.core.common.provider.CoreProvider
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import eu.peernetwork.app.service.NetworkService
import eu.peernetwork.core.remote.api.RequestClient
import eu.peernetwork.user.ui.provider.UserProvider
import eu.peernetwork.wallet.ui.provider.WalletProvider

interface ApplicationProvider : UserProvider, ResourceProvider, ContentProvider, WalletProvider, CoreProvider {
    fun context(): Context

    fun firebaseRemoteConfig(): FirebaseRemoteConfig

    fun networkProvider(): RequestClient

    fun networkResource(): NetworkService
}
