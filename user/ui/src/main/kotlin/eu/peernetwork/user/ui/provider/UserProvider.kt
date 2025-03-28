package eu.peernetwork.user.ui.provider

import eu.peernetwork.core.common.concurrent.Dispatcher
import eu.peernetwork.user.domain.provider.AccountProvider
import eu.peernetwork.user.domain.provider.AuthenticationProvider

interface UserProvider : Dispatcher.Provider, AccountProvider, AuthenticationProvider
