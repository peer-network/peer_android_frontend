package eu.peernetwork.app.usecase

import com.google.gson.Gson
import eu.peernetwork.app.model.Properties
import eu.peernetwork.core.common.provider.Dispatcher
import eu.peernetwork.core.common.usecase.SuspendableUseCase
import eu.peernetwork.user.domain.repository.ResourceRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OnboardingUsecase @Inject constructor(
    private val gson: Gson,
    private val dispatcher: Dispatcher,
    private val repository: ResourceRepository,
) : SuspendableUseCase<Properties> {
    override suspend fun invoke(): Properties = withContext(dispatcher.io) {
        val response = repository.string("/assets/constants.json")
            .replace("""\\""".toRegex(), """\\\\""")
        gson.fromJson(response, Properties::class.java)
    }
}
