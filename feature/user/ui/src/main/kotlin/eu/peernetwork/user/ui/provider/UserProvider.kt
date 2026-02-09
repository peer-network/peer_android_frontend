package eu.peernetwork.user.ui.provider

import eu.peernetwork.core.common.provider.CoreProvider
import eu.peernetwork.user.domain.provider.AccountProvider
import eu.peernetwork.user.domain.provider.AuthenticationProvider

interface UserProvider : CoreProvider, AccountProvider, AuthenticationProvider
