package eu.peernetwork.app.module.blog

import dagger.Binds
import dagger.Module
import eu.peernetwork.blog.data.interactor.AuthorInteractorDelegate
import eu.peernetwork.blog.data.interactor.CommentInteractorDelegate
import eu.peernetwork.blog.data.interactor.ContentInteractorDelegate
import eu.peernetwork.blog.data.interactor.ContentMultipartInteractorDelegate
import eu.peernetwork.blog.data.interactor.EngagementInteractorDelegate
import eu.peernetwork.blog.domain.interactor.AuthorInteractor
import eu.peernetwork.blog.domain.interactor.CommentInteractor
import eu.peernetwork.blog.domain.interactor.ContentInteractor
import eu.peernetwork.blog.domain.interactor.ContentMultipartInteractor
import eu.peernetwork.blog.domain.interactor.EngagementInteractor
import javax.inject.Singleton

@Module(
    includes = [
        ApiModule::class,
        RepositoryModule::class
    ]
)
interface BlogModule {
    @Binds
    fun bindAuthorInteractor(delegate: AuthorInteractorDelegate): AuthorInteractor

    @Binds
    @Singleton
    fun bindEngagementInteractor(delegate: EngagementInteractorDelegate): EngagementInteractor

    @Binds
    fun bindContentInteractor(delegate: ContentInteractorDelegate): ContentInteractor

    @Binds
    fun bindCommentInteractor(delegate: CommentInteractorDelegate): CommentInteractor

    @Binds
    fun bindContentMultipartInteractor(delegate: ContentMultipartInteractorDelegate): ContentMultipartInteractor
}
