package eu.peernetwork.blog.data.interactor

import eu.peernetwork.blog.domain.interactor.ContentInteractor
import eu.peernetwork.blog.domain.model.Content
import eu.peernetwork.blog.domain.model.Draft
import eu.peernetwork.blog.domain.repository.ContentRepository
import eu.peernetwork.wallet.domain.repository.RewardRepository
import javax.inject.Inject

class ContentInteractorDelegate @Inject constructor(
    private val repository: ContentRepository,
    private val rewardRepository: RewardRepository
) : ContentInteractor {
    override suspend fun create(draft: Draft): Content {
        val content = repository.create(draft)
        try {
            rewardRepository.get()
        } catch (error: Throwable) {
            error.printStackTrace()
        }
        return content
    }
}
