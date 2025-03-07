package eu.peernetwork.user.data.api

import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.user.domain.model.Account

interface SearchApi {
    suspend fun findByUsername(username: String, pageable: Pageable): List<Account>
}
