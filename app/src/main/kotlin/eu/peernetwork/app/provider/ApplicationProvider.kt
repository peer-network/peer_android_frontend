package eu.peernetwork.app.provider

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import eu.peernetwork.app.service.NetworkResource
import eu.peernetwork.core.remote.provider.NetworkProvider
import eu.peernetwork.user.ui.provider.UserProvider
import eu.peernetwork.wallet.ui.provider.WalletProvider

interface ApplicationProvider : UserProvider, ResourceProvider, ContentProvider, WalletProvider {
    fun firebaseRemoteConfig(): FirebaseRemoteConfig

    fun networkProvider(): NetworkProvider

    fun networkResource(): NetworkResource
}
