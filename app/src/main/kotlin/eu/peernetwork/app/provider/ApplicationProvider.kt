package eu.peernetwork.app.provider

import eu.peernetwork.user.ui.provider.UserProvider
import eu.peernetwork.wallet.ui.provider.WalletProvider

interface ApplicationProvider : UserProvider, ResourceProvider, ContentProvider, WalletProvider
