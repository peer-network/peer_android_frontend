package eu.peernetwork.core.ui.usecase

import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.paging.PagingSource.LoadParams
import androidx.paging.PagingSource.LoadResult
import eu.peernetwork.core.common.usecase.ParameterizedObservableUseCase

abstract class PagingUsecase<P, T : Any> : ParameterizedObservableUseCase<P, PagingData<T>> {
    fun source(): PagingSource<Int, T> {
        return object : PagingSource<Int, T>() {
            override fun getRefreshKey(state: PagingState<Int, T>): Int? = null

            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, T> {
                return try {
                    getData(params)
                } catch (error: Throwable) {
                    LoadResult.Error(error)
                }
            }
        }
    }

    abstract suspend fun getData(params: LoadParams<Int>): LoadResult<Int, T>
}
