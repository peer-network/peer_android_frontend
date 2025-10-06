package eu.peernetwork.blog.ui.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource.LoadParams
import androidx.paging.PagingSource.LoadResult
import eu.peernetwork.blog.domain.repository.CommentRepository
import eu.peernetwork.blog.ui.mapper.mapToComment
import eu.peernetwork.blog.ui.model.UiComment
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.core.ui.exception.NoContentException
import eu.peernetwork.core.ui.usecase.AnnotationUsecase
import eu.peernetwork.core.ui.usecase.PagingUsecase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CommentsUsecase @Inject constructor(
    private val repository: CommentRepository,
    private val annotationUsecase: AnnotationUsecase
) : PagingUsecase<CommentsUsecase.Parameter, UiComment>() {
    private lateinit var param: Parameter

    override fun invoke(param: Parameter): Flow<PagingData<UiComment>> {
        this.param = param
        return Pager(
            config = PagingConfig(
                pageSize = param.page.limit,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { source() }
        ).flow
    }

    override suspend fun getData(params: LoadParams<Int>): LoadResult<Int, UiComment> {
        val currentOffset = params.key ?: param.page.offset
        val currentPage = Pageable(
            offset = currentOffset,
            limit = param.page.limit
        )
        val response = repository.getAll(
            id = param.id,
            page = currentPage
        )
        return if (response.items.isEmpty() && currentPage.offset == 0) {
            LoadResult.Error(NoContentException())
        } else {
            LoadResult.Page(
                data = response.items.map { it.mapToComment { annotationUsecase(it) } },
                prevKey = if (currentOffset <= 0) null else currentOffset - 1,
                nextKey = if (response.items.isEmpty()) null else currentOffset + response.items.size
            )
        }
    }

    data class Parameter(
        val id: String,
        val page: Pageable
    )
}