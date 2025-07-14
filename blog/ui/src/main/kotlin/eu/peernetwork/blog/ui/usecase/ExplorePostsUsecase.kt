package eu.peernetwork.blog.ui.usecase

import android.content.Context
import androidx.compose.ui.text.AnnotatedString
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource.LoadParams
import androidx.paging.PagingData
import androidx.paging.PagingSource.LoadResult
import eu.peernetwork.blog.domain.model.Filter.Criteria
import eu.peernetwork.blog.domain.usecase.ExploreUsecase
import eu.peernetwork.blog.ui.mapper.mapToPhoto
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.core.common.provider.Dispatcher
import eu.peernetwork.core.ui.exception.NoContentException
import eu.peernetwork.core.ui.usecase.PagingUsecase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ExplorePostsUsecase @Inject constructor(
    private val context: Context,
    private val usecase: ExploreUsecase,
    private val dispatcher: Dispatcher
): PagingUsecase<ExplorePostsUsecase.Parameter, UiPost>() {
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
            ExploreUsecase.Parameter(
                criteria = param.criteria,
                page = currentPage
            )
        )
        if (response.items.isEmpty() && currentPage.offset == 0) {
            LoadResult.Error(NoContentException())
        } else {
            LoadResult.Page(
                data = response.items.map { it.mapToPhoto(context, annotate = { str -> AnnotatedString(str) }) },
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
        val criteria: Criteria? = null,
        val page: Pageable
    )
}