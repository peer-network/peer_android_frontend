package eu.peernetwork.core.common.resource

import eu.peernetwork.core.common.service.ResourceService
import eu.peernetwork.core.common.usecase.ParameterizedImmediateUseCase
import javax.inject.Inject

class StringResource @Inject constructor(
    private val service: ResourceService
) : ParameterizedImmediateUseCase<String, String> {
    override fun invoke(param: String): String {
        return service.string(param)
    }
}
