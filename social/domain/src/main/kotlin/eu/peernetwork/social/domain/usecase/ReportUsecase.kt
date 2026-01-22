package eu.peernetwork.social.domain.usecase

import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import eu.peernetwork.social.domain.repository.ModerationRepository
import javax.inject.Inject

class ReportUsecase @Inject constructor(
    private val repository: ModerationRepository
): ParameterizedSuspendableUseCase<String, String> {
    override suspend fun invoke(param: String): String {
        return repository.report(param)
    }
}
