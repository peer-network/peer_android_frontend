package eu.peernetwork.app.provider

import android.content.Context
import eu.peernetwork.core.common.provider.CoreProvider
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import eu.peernetwork.app.service.NetworkResource
import eu.peernetwork.core.remote.provider.NetworkProvider
import eu.peernetwork.user.ui.provider.UserProvider
import eu.peernetwork.wallet.ui.provider.WalletProvider

interface ApplicationProvider : UserProvider, ResourceProvider, ContentProvider, WalletProvider, CoreProvider {
    fun context(): Context

    fun firebaseRemoteConfig(): FirebaseRemoteConfig

    fun networkProvider(): NetworkProvider

    fun networkResource(): NetworkResource
}
