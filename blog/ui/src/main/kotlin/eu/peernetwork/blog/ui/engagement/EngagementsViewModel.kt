package eu.peernetwork.blog.ui.engagement

import androidx.lifecycle.ViewModel
import eu.peernetwork.blog.domain.model.Engagement
import eu.peernetwork.blog.domain.usecase.DislikeUsecase
import eu.peernetwork.blog.domain.usecase.LikeUsecase
import eu.peernetwork.blog.domain.usecase.ViewUsecase
import javax.inject.Inject

class EngagementsViewModel @Inject constructor(
    private val likeUsecase: LikeUsecase,
    private val dislikeUsecase: DislikeUsecase,
    private val viewUsecase: ViewUsecase
) : ViewModel() {
    suspend fun create(id: String, engagement: Engagement.Content) {
        when (engagement) {
            is Engagement.Content.Like -> {
                likeUsecase(id)
            }
            is Engagement.Content.Dislike -> {
                dislikeUsecase(id)
            }
            is Engagement.Content.View -> {
                viewUsecase(id)
            }
        }
    }
}