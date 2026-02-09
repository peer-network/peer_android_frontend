package eu.peernetwork.blog.data.interactor

import eu.peernetwork.blog.domain.interactor.CommentInteractor
import eu.peernetwork.blog.domain.model.Comment
import eu.peernetwork.blog.domain.repository.CommentRepository
import eu.peernetwork.wallet.domain.repository.RewardRepository
import javax.inject.Inject

class CommentInteractorDelegate @Inject constructor(
    private val repository: CommentRepository,
    private val rewardRepository: RewardRepository
) : CommentInteractor {
    override suspend fun comment(
        postId: String,
        text: String
    ): Comment {
        val content = repository.comment(postId, text)
        try {
            rewardRepository.get()
        } catch (error: Throwable) {
            error.printStackTrace()
        }
        return content
    }
}
