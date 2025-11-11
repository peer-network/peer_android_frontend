package eu.peernetwork.blog.ui.usecase

import android.content.Context
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource.LoadParams
import androidx.paging.PagingSource.LoadResult
import eu.peernetwork.blog.domain.model.Filter.Criteria
import eu.peernetwork.blog.domain.model.Category
import eu.peernetwork.blog.domain.model.Content
import eu.peernetwork.blog.domain.usecase.AdvertUsecase
import eu.peernetwork.blog.domain.usecase.PostUsecase.Companion.FEED
import eu.peernetwork.blog.ui.mapper.mapToPhoto
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.core.common.provider.Dispatcher
import eu.peernetwork.core.ui.exception.NoContentException
import eu.peernetwork.core.ui.usecase.AnnotationUsecase
import eu.peernetwork.core.ui.usecase.PagingUsecase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FeedAdvertUsecase @Inject constructor(
    private val context: Context,
    private val usecase: AdvertUsecase,
    private val dispatcher: Dispatcher,
    private val annotationUsecase: AnnotationUsecase
) : PagingUsecase<FeedAdvertUsecase.Parameter, UiPost>() {
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
            limit = params.loadSize
        )
        val response = usecase(
            AdvertUsecase.Parameter(
                types = param.types,
                category = param.category,
                criteria = param.criteria,
                page = currentPage
            )
        )
        if (response.items.isEmpty() && currentPage.offset == 0) {
            LoadResult.Error(NoContentException())
        } else {
            LoadResult.Page(
                data = response.items.map { photo ->
                    photo.mapToPhoto(context) { annotationUsecase(it) }
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
        val types: Set<Content.Type> = FEED,
        val category: Category = Category.NONE,
        val criteria: Criteria? = null,
        val page: Pageable
    )
}