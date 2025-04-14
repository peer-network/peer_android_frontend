package eu.peernetwork.blog.ui.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import eu.peernetwork.blog.domain.model.Comment
import eu.peernetwork.blog.domain.repository.CommentRepository
import eu.peernetwork.blog.ui.mapper.mapToComment
import eu.peernetwork.blog.ui.model.UiComment
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.core.ui.usecase.PagingUsecase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CommentUsecase @Inject constructor(
    private val repository: CommentRepository
) : PagingUsecase<CommentUsecase.Parameter, UiComment>() {
    private lateinit var param: Parameter

    override fun invoke(param: Parameter): Flow<PagingData<UiComment>> {
        this.param = param
        return Pager(
            config = PagingConfig(
                pageSize = param.page.limit,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { this }
        ).flow
    }

    override suspend fun getData(params: LoadParams<Int>): LoadResult<Int, UiComment> {
        val currentOffset = params.key ?: param.page.offset
        val currentPage = Pageable(
            offset = currentOffset,
            limit = params.loadSize
        )
        val response = repository.getAll(
            id = param.id,
            page = currentPage
        )

        return LoadResult.Page(
            data = response.items.map { it.mapToComment() },
            prevKey = if (currentOffset <= 0) null else currentOffset - 1,
            nextKey = if (response.items.isEmpty()) null else currentOffset + response.items.size
        )
    }

    suspend fun invoke(postId: String, text: String): Comment {
        return repository.comment(postId, text)
    }

    data class Parameter(
        val id: String,
        val page: Pageable
    )
}