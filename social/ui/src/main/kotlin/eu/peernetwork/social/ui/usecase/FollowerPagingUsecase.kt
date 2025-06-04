package eu.peernetwork.social.ui.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource.LoadParams
import androidx.paging.PagingSource.LoadResult
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.core.common.provider.Dispatcher
import eu.peernetwork.core.ui.exception.NoContentException
import eu.peernetwork.core.ui.usecase.PagingUsecase
import eu.peernetwork.social.domain.usecase.FollowersUsecase
import eu.peernetwork.social.ui.mapper.mapFromDomain
import eu.peernetwork.social.ui.model.UiMember
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FollowerPagingUsecase @Inject constructor(
    private val dispatcher: Dispatcher,
    private val usecase: FollowersUsecase
) : PagingUsecase<FollowerPagingUsecase.Parameter, UiMember>() {
    private lateinit var param: Parameter

    override fun invoke(param: Parameter): Flow<PagingData<UiMember>> {
        this.param = param
        return Pager(
            config = PagingConfig(
                pageSize = param.page.limit,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { source() }
        ).flow
    }

    override suspend fun getData(params: LoadParams<Int>): LoadResult<Int, UiMember> = withContext(dispatcher.io) {
        val currentOffset = params.key ?: param.page.offset
        val currentPage = Pageable(
            offset = currentOffset,
            limit = param.page.limit
        )
        val response = usecase(FollowersUsecase.Params(param.id, currentPage))
        if (response.items.isEmpty() && currentPage.offset == 0) {
            LoadResult.Error(NoContentException())
        } else {
            LoadResult.Page(
                data = response.items.map { it.mapFromDomain() },
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
