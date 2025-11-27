package eu.peernetwork.blog.ui.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource.LoadParams
import androidx.paging.PagingSource.LoadResult
import eu.peernetwork.blog.domain.model.Filter.Criteria
import eu.peernetwork.blog.domain.model.Category
import eu.peernetwork.blog.domain.model.Content
import eu.peernetwork.blog.domain.usecase.PostUsecase
import eu.peernetwork.blog.domain.usecase.PostUsecase.Companion.POST
import eu.peernetwork.blog.ui.mapper.v2.mapFromDomain
import eu.peernetwork.blog.ui.model.v2.UiPost
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.core.common.provider.Dispatcher
import eu.peernetwork.core.ui.exception.NoContentException
import eu.peernetwork.core.ui.usecase.PagingUsecase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthorPostUsecase @Inject constructor(
    private val dispatcher: Dispatcher,
    private val usecase: PostUsecase,
) : PagingUsecase<AuthorPostUsecase.Parameter, UiPost>() {
    private lateinit var param: Parameter

    override fun invoke(param: Parameter): Flow<PagingData<UiPost>> {
        this.param = param
        return Pager(
            config = PagingConfig(
                pageSize = param.page.limit,
                prefetchDistance = param.page.limit,
                initialLoadSize = param.page.limit,
                enablePlaceholders = false,
            ),
            pagingSourceFactory = { source() }
        ).flow
    }

    override suspend fun getData(
        params: LoadParams<Int>
    ): LoadResult<Int, UiPost> = withContext(dispatcher.io) {
        val currentOffset = params.key ?: param.page.offset
        val currentPage = Pageable(
            offset = currentOffset,
            limit = param.page.limit
        )
        val response = usecase(
            PostUsecase.Parameter(
                types = param.types,
                author = param.author,
                criteria = param.criteria,
                page = currentPage
            )
        )
        if (response.items.isEmpty() && currentPage.offset == 0) {
            LoadResult.Error(NoContentException())
        } else {
            LoadResult.Page(
                data = response.items.map { photo ->
                    photo.mapFromDomain()
                },
                prevKey = if (currentOffset != param.page.offset) {
                    (currentOffset - params.loadSize).coerceAtLeast(0)
                } else {
                    null
                },
                nextKey = if (response.items.isNotEmpty()) {
                    currentOffset + response.items.size
                } else {
                    null
                }
            )
        }
    }

    data class Parameter(
        val author: String,
        val types: Set<Content.Type> = POST,
        val category: Category = Category.NONE,
        val criteria: Criteria? = null,
        val page: Pageable
    )
}
