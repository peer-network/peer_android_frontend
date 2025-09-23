package eu.peernetwork.app.usecase

import com.google.gson.Gson
import eu.peernetwork.app.model.Properties
import eu.peernetwork.core.common.usecase.SuspendableUseCase
import eu.peernetwork.user.domain.repository.ResourceRepository
import javax.inject.Inject

class OnboardingUsecase @Inject constructor(
    private val gson: Gson,
    private val repository: ResourceRepository,
) : SuspendableUseCase<Properties> {
    override suspend fun invoke(): Properties {
        val response = repository.string("/assets/constants.json")
        return gson.fromJson(response, Properties::class.java)
    }
}
