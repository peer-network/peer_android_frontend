package eu.peernetwork.app.module.blog

import dagger.Binds
import dagger.Module
import eu.peernetwork.blog.data.repository.CommentRepositoryDelegate
import eu.peernetwork.blog.data.repository.ContentRepositoryDelegate
import eu.peernetwork.blog.data.repository.EligibilityRepositoryDelegate
import eu.peernetwork.blog.data.repository.EngagementRepositoryDelegate
import eu.peernetwork.blog.data.repository.MultipartRepositoryDelegate
import eu.peernetwork.blog.domain.repository.CommentRepository
import eu.peernetwork.blog.domain.repository.ContentRepository
import eu.peernetwork.blog.domain.repository.EligibilityRepository
import eu.peernetwork.blog.domain.repository.EngagementRepository
import eu.peernetwork.blog.domain.repository.MultipartRepository

@Module
interface RepositoryModule {
    @Binds
    fun bindContentRepository(delegate: ContentRepositoryDelegate): ContentRepository

    @Binds
    fun bindCommentRepository(delegate: CommentRepositoryDelegate): CommentRepository

    @Binds
    fun bindEngagementRepository(delegate: EngagementRepositoryDelegate): EngagementRepository

    @Binds
    fun bindEligibilityRepository(delegate: EligibilityRepositoryDelegate): EligibilityRepository

    @Binds
    fun bindContentMultipartRepository(delegate: MultipartRepositoryDelegate): MultipartRepository
}
