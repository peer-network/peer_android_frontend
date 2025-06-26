package eu.peernetwork.app.service

import com.google.gson.Gson
import eu.peernetwork.app.model.ResponseCode
import eu.peernetwork.core.common.provider.Dispatcher
import eu.peernetwork.core.common.service.ResourceService
import eu.peernetwork.user.domain.repository.ResourceRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ResourceServiceDelegate @Inject constructor(
    private val gson: Gson,
    private val dispatcher: Dispatcher,
    private val resourceRepository: ResourceRepository
) : ResourceService, BootstrapService {
    private var mapper: Map<String, ResponseCode.Message> = mapOf<String, ResponseCode.Message>()

    override fun isReady(): Boolean {
        return mapper.isNotEmpty()
    }

    override suspend fun initialize() = withContext(dispatcher.io) {
        val response = resourceRepository.string("/assets/response-codes.json")
        val model = gson.fromJson(response, ResponseCode::class.java)
        mapper = model.data
    }

    override fun string(key: String): String {
        return mapper.getOrDefault(key, null)?.userFriendlyComment ?: key
    }
}
