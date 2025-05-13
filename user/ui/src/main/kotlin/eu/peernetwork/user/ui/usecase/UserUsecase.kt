package eu.peernetwork.user.ui.usecase

import eu.peernetwork.core.common.provider.Dispatcher
import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import eu.peernetwork.user.domain.model.Account
import eu.peernetwork.user.domain.usecase.DescriptionUsecase
import eu.peernetwork.user.ui.mapper.mapFromDomain
import eu.peernetwork.user.ui.model.UiAccount
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserUsecase @Inject constructor(
    private val dispatcher: Dispatcher,
    private val descriptionUsecase: DescriptionUsecase,
) : ParameterizedSuspendableUseCase<Account, UiAccount?> {
    override suspend fun invoke(param: Account): UiAccount {
        return withContext(dispatcher.io) {
            val response = param.mapFromDomain()
            val description = response.bio?.let { getDescription(it) }
            response.copy(bio = description)
        }
    }

    private suspend fun getDescription(path: String): String? {
        return try {
            descriptionUsecase(path).trim()
                .replace(Regex("[\\s\\t]+"), " ")
                .replace(Regex("^\\s+|\\s+$"), "")
        } catch (_: Throwable) { null }
    }
}
