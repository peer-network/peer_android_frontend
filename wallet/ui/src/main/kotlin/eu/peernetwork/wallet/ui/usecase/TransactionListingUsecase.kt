package eu.peernetwork.wallet.ui.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingSource.LoadResult
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.core.ui.exception.NoContentException
import eu.peernetwork.core.ui.usecase.PagingUsecase
import eu.peernetwork.wallet.domain.model.Filter
import eu.peernetwork.wallet.domain.model.Sort
import eu.peernetwork.wallet.domain.usecase.TransactionsUsecase
import eu.peernetwork.wallet.ui.mapper.mapToTransaction
import eu.peernetwork.wallet.ui.model.UiTransaction
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TransactionListingUsecase @Inject constructor(
    private val usecase: TransactionsUsecase
) : PagingUsecase<TransactionListingUsecase.Parameter, UiTransaction>() {
    private lateinit var param: Parameter

    override fun invoke(param: Parameter): Flow<PagingData<UiTransaction>> {
        this.param = param
        return Pager(
            config = PagingConfig(
                pageSize = param.page.limit,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { source() }
        ).flow
    }

    override suspend fun getData(
        params: PagingSource.LoadParams<Int>
    ): LoadResult<Int, UiTransaction> {
        val currentOffset = params.key ?: param.page.offset
        val currentPage = Pageable(
            offset = currentOffset,
            limit = param.page.limit
        )
        val response = usecase(
            param = TransactionsUsecase.Parameter(
                filter = param.filter,
                sort = param.sort,
                page = currentPage
            )
        )
        return if (response.items.isEmpty() && currentPage.offset == 0) {
            LoadResult.Error(NoContentException())
        } else {
            LoadResult.Page(
                data = response.items.map { it.mapToTransaction() },
                prevKey = if (currentOffset <= 0) null else currentOffset - 1,
                nextKey = if (response.items.isEmpty()) null else currentOffset + response.items.size
            )
        }
    }

    data class Parameter(
        val filter: Filter,
        val sort: Sort,
        val page: Pageable
    )
}
