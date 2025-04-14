package eu.peernetwork.core.ui.usecase

import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import eu.peernetwork.core.common.usecase.ParameterizedObservableUseCase

abstract class PagingUsecase<P, T : Any> : ParameterizedObservableUseCase<P, PagingData<T>>,
    PagingSource<Int, T>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
        return try {
            getData(params)
        } catch (error: Throwable) {
            LoadResult.Error(error)
        }
    }

    abstract suspend fun getData(params: LoadParams<Int>): LoadResult<Int, T>

    override fun getRefreshKey(state: PagingState<Int, T>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val closestPage = state.closestPageToPosition(anchorPosition)
            closestPage?.prevKey?.plus(1) ?: closestPage?.nextKey?.minus(1)
        }
    }
}
