package eu.peernetwork.app.service

import com.google.gson.Gson
import eu.peernetwork.app.interactor.RemoteInteractor
import eu.peernetwork.app.model.ResponseCode
import eu.peernetwork.core.common.provider.Dispatcher
import eu.peernetwork.core.common.interactor.ResourceInteractor
import eu.peernetwork.user.domain.repository.ResourceRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class ResourceInteractorDelegate @Inject constructor(
    private val gson: Gson,
    private val dispatcher: Dispatcher,
    private val resourceRepository: ResourceRepository,
    @Named("baseUrl") private val baseUrl: String
) : ResourceInteractor, BootstrapService, RemoteInteractor {
    private var _baseUrl: String = baseUrl

    private var mapper: Map<String, ResponseCode.Message> = mapOf<String, ResponseCode.Message>()

    override fun isReady(): Boolean {
        return mapper.isNotEmpty()
    }

    override suspend fun initialize() = withContext(dispatcher.io) {
        val response = resourceRepository.string("/assets/response-codes.json")
        val model = gson.fromJson(response, ResponseCode::class.java)
        mapper = model.data
    }

    override fun getBaseUrl(): String = _baseUrl

    override fun setBaseUrl(url: String) {
        _baseUrl = url
    }

    override fun string(key: String): String {
        return mapper.getOrDefault(key, null)?.userFriendlyComment ?: key
    }
}
