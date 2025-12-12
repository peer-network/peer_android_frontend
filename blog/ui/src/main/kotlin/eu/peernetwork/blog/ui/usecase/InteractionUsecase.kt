package eu.peernetwork.blog.ui.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource.LoadParams
import androidx.paging.PagingSource.LoadResult
import eu.peernetwork.blog.domain.model.Engagement
import eu.peernetwork.blog.domain.usecase.ContentInteractorUsecase
import eu.peernetwork.blog.ui.mapper.mapFromDomain
import eu.peernetwork.blog.ui.model.UiAuthor
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.core.common.provider.Dispatcher
import eu.peernetwork.core.ui.exception.NoContentException
import eu.peernetwork.core.ui.usecase.PagingUsecase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class InteractionUsecase @Inject constructor(
    private val dispatcher: Dispatcher,
    private val usecase: ContentInteractorUsecase
) : PagingUsecase<InteractionUsecase.Parameter, UiAuthor>() {
    private lateinit var param: Parameter

    override fun invoke(param: Parameter): Flow<PagingData<UiAuthor>> {
        this.param = param
        return Pager(
            config = PagingConfig(
                pageSize = param.page.limit,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { source() }
        ).flow
    }

    override suspend fun getData(params: LoadParams<Int>): LoadResult<Int, UiAuthor> = withContext(dispatcher.io) {
        val currentOffset = params.key ?: param.page.offset
        val currentPage = Pageable(
            offset = currentOffset,
            limit = param.page.limit
        )
        val response = usecase(
            ContentInteractorUsecase.Parameter(
                id = param.id,
                engagement = param.engagement,
                page = currentPage
            )
        )
        if (response.items.isEmpty() && currentPage.offset == 0) {
            LoadResult.Error(NoContentException())
        } else {
            LoadResult.Page(
                data = response.items.map { it.mapFromDomain() },
                prevKey = if (currentOffset <= 0) null else currentOffset - 1,
                nextKey = if (response.items.isEmpty() || response.items.size < param.page.limit) {
                    null
                } else {
                    currentOffset + response.items.size
                }
            )
        }
    }

    data class Parameter(
        val id: String,
        val engagement: Engagement.Content,
        val page: Pageable
    )
}