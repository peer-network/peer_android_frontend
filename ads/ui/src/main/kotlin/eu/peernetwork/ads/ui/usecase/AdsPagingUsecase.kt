package eu.peernetwork.ads.ui.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingSource.LoadResult
import eu.peernetwork.ads.domain.model.Ads
import eu.peernetwork.ads.domain.model.Filter
import eu.peernetwork.ads.domain.usecase.AdsListingUsecase
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.core.ui.exception.NoContentException
import eu.peernetwork.core.ui.usecase.PagingUsecase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AdsPagingUsecase @Inject constructor(
    private val usecase: AdsListingUsecase
) : PagingUsecase<AdsPagingUsecase.Parameter, Ads>() {
    private lateinit var param: Parameter

    override fun invoke(param: Parameter): Flow<PagingData<Ads>> {
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

    override suspend fun getData(params: PagingSource.LoadParams<Int>): LoadResult<Int, Ads> {
        val currentOffset = params.key ?: param.page.offset
        val currentPage = Pageable(
            offset = currentOffset,
            limit = param.page.limit
        )
        val response = usecase(
            AdsListingUsecase.Parameter(
                filter = param.filter,
                page = currentPage
            )
        )
        if (response.items.isEmpty() && currentPage.offset == 0) {
            return LoadResult.Error(NoContentException())
        }
        return LoadResult.Page(
            data = response.items,
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

    data class Parameter(
        val filter: Filter = Filter(),
        val page: Pageable
    )
}