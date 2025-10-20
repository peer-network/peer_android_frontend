package eu.peernetwork.blog.ui.usecase

import eu.peernetwork.blog.domain.repository.MultipartRepository
import eu.peernetwork.core.common.provider.Dispatcher
import eu.peernetwork.core.common.usecase.ParameterizedSuspendableUseCase
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class MultipartUsecase @Inject constructor(
    private val dispatcher: Dispatcher,
    private val repository: MultipartRepository,
) : ParameterizedSuspendableUseCase<File, String> {
    override suspend fun invoke(param: File): String = withContext(dispatcher.io) {
       repository.upload(param)
    }
}