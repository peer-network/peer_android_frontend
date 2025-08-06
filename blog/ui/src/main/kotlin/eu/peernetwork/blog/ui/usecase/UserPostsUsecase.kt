package eu.peernetwork.blog.ui.usecase

import android.content.Context
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource.LoadParams
import androidx.paging.PagingSource.LoadResult
import eu.peernetwork.blog.domain.model.Filter.Criteria
import eu.peernetwork.blog.domain.model.Category
import eu.peernetwork.blog.domain.usecase.PhotosUsecase
import eu.peernetwork.blog.domain.usecase.EngagementRefreshUsecase
import eu.peernetwork.blog.ui.mapper.mapToPhoto
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.core.common.provider.Dispatcher
import eu.peernetwork.core.ui.exception.NoContentException
import eu.peernetwork.core.ui.usecase.AnnotationUsecase
import eu.peernetwork.core.ui.usecase.PagingUsecase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserPostsUsecase @Inject constructor(
    private val context: Context,
    private val usecase: PhotosUsecase,
    private val dispatcher: Dispatcher,
    private val engagementRefreshUsecase: EngagementRefreshUsecase,
    private val annotationUsecase: AnnotationUsecase
) : PagingUsecase<UserPostsUsecase.Parameter, UiPost>() {
    private lateinit var param: Parameter

    override fun invoke(param: Parameter): Flow<PagingData<UiPost>> {
        this.param = param
        return Pager(
            config = PagingConfig(
                pageSize = param.page.limit,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { source() }
        ).flow
    }

    override suspend fun getData(params: LoadParams<Int>): LoadResult<Int, UiPost> = withContext(dispatcher.io) {
        val currentOffset = params.key ?: param.page.offset
        val currentPage = Pageable(
            offset = currentOffset,
            limit = param.page.limit
        )
        val response = usecase(
            PhotosUsecase.Parameter(
                mode = param.mode,
                category = param.category,
                criteria = param.criteria,
                page = currentPage
            )
        )
        if (currentOffset <= 0) {
            engagementRefreshUsecase()
        }
        if (response.items.isEmpty() && currentPage.offset == 0) {
            LoadResult.Error(NoContentException())
        } else {
            LoadResult.Page(
                data = response.items.map { it.mapToPhoto(context) { annotationUsecase(it) } },
                prevKey = if (currentOffset <= 0) null else currentOffset - 1,
                nextKey = if (response.items.isNotEmpty()) {
                    currentOffset + response.items.size
                } else {
                    null
                }
            )
        }
    }

    data class Parameter(
        val mode: String? = null,
        val category: Category = Category.ALL,
        val criteria: Criteria? = null,
        val page: Pageable
    )
}