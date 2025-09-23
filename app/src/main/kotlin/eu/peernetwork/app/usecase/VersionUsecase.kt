package eu.peernetwork.app.usecase

import com.google.gson.Gson
import eu.peernetwork.app.BuildConfig
import eu.peernetwork.app.exception.VersionException
import eu.peernetwork.app.interactor.RemoteInteractor
import eu.peernetwork.app.model.Endpoint
import eu.peernetwork.core.common.provider.Dispatcher
import eu.peernetwork.core.common.usecase.SuspendableUseCase
import eu.peernetwork.user.domain.repository.ResourceRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

class VersionUseCase @Inject constructor(
    private val gson: Gson,
    private val dispatcher: Dispatcher,
    private val interactor: RemoteInteractor,
    private val repository: ResourceRepository
) : SuspendableUseCase<Unit> {

    override suspend fun invoke(): Unit = withContext(dispatcher.io) {
        val response = repository.string("/assets/endpoints.json")
            .replace("""\\""".toRegex(), """\\\\""")
        val endpoint = gson.fromJson(response, Endpoint::class.java)
        endpoint.platform.configuration.firstOrNull()?.let {
            if (!isOutdated(it.version)) {
                interactor.setBaseUrl(it.url)
            } else {
                throw VersionException(it.version)
            }
        }
    }

    private fun isOutdated(required: String): Boolean {
        val currentBase = BuildConfig.VERSION_NAME.split("-").first()
        val requiredBase = required.split("-").first()
        val currentParts = currentBase.split(".").mapNotNull { it.toIntOrNull() }
        val requiredParts = requiredBase.split(".").mapNotNull { it.toIntOrNull() }
        if (currentParts.isEmpty() || requiredParts.isEmpty()) {
            return currentBase < requiredBase
        }
        for (i in 0 until maxOf(currentParts.size, requiredParts.size)) {
            val currentPart = currentParts.getOrElse(i) { 0 }
            val requiredPart = requiredParts.getOrElse(i) { 0 }

            when {
                currentPart < requiredPart -> return true
                currentPart > requiredPart -> return false
            }
        }
        return false
    }
}
