package eu.peernetwork.blog.ui.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import eu.peernetwork.blog.domain.model.Content
import eu.peernetwork.blog.domain.model.Filter
import eu.peernetwork.blog.domain.repository.ContentRepository
import eu.peernetwork.blog.ui.mapper.mapToPhoto
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.core.ui.usecase.PagingUsecase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PostUsecase @Inject constructor(
    private val repository: ContentRepository
) : PagingUsecase<PostUsecase.Parameter, UiPost>() {
    private lateinit var param: Parameter

    override fun invoke(param: Parameter): Flow<PagingData<UiPost>> {
        this.param = param
        return Pager(
            config = PagingConfig(
                pageSize = param.page.limit,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { this }
        ).flow
    }

    override suspend fun getData(params: LoadParams<Int>): LoadResult<Int, UiPost> {
        val currentOffset = params.key ?: param.page.offset
        val currentPage = Pageable(
            offset = currentOffset,
            limit = params.loadSize
        )
        val response = repository.getAll(
            filter = Filter(
                type = setOf(
                    Content.Type.TEXT,
                    Content.Type.IMAGE
                )
            ),
            currentPage
        )
        return LoadResult.Page(
            data = response.items.map { it.mapToPhoto() },
            prevKey = if (currentOffset <= 0) null else currentOffset - 1,
            nextKey = if (response.items.isEmpty()) null else currentOffset + response.items.size
        )
    }

    data class Parameter(
        val page: Pageable
    )
}