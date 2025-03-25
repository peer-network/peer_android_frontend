package eu.peernetwork.user.domain.provider

import eu.peernetwork.user.domain.repository.AccountRepository
import eu.peernetwork.user.domain.repository.ResourceRepository
import eu.peernetwork.user.domain.repository.SearchRepository

interface AccountProvider {
    fun accountRepository(): AccountRepository

    fun searchRepository(): SearchRepository

    fun resourceRepository(): ResourceRepository
}
