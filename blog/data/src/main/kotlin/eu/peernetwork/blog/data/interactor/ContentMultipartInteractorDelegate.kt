package eu.peernetwork.blog.data.interactor

import eu.peernetwork.blog.domain.interactor.ContentInteractor
import eu.peernetwork.blog.domain.interactor.ContentMultipartInteractor
import eu.peernetwork.blog.domain.model.Content
import eu.peernetwork.blog.domain.model.Draft
import eu.peernetwork.blog.domain.repository.ContentMultipartRepository
import eu.peernetwork.blog.domain.repository.ContentRepository
import eu.peernetwork.wallet.domain.repository.RewardRepository
import javax.inject.Inject

class ContentMultipartInteractorDelegate @Inject constructor(
    private val repository: ContentMultipartRepository,
    private val rewardRepository: RewardRepository
) : ContentMultipartInteractor {
    override suspend fun upload(file: Draft): Content {
        val content = repository.upload(file)
        try {
            rewardRepository.get()
        } catch (error: Throwable) {
            error.printStackTrace()
        }
        return content
    }
}
