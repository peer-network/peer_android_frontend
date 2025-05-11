package eu.peernetwork.blog.ui.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource.LoadParams
import androidx.paging.PagingSource.LoadResult
import eu.peernetwork.blog.domain.usecase.VideosUsecase
import eu.peernetwork.blog.ui.mapper.mapToVideo
import eu.peernetwork.blog.ui.model.UiVideo
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.core.ui.usecase.AnnotationUsecase
import eu.peernetwork.core.ui.usecase.PagingUsecase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthorVideoUsecase @Inject constructor(
    private val usecase: VideosUsecase,
    private val annotationUsecase: AnnotationUsecase
) : PagingUsecase<AuthorVideoUsecase.Parameter, UiVideo>() {
    private lateinit var param: Parameter

    override fun invoke(param: Parameter): Flow<PagingData<UiVideo>> {
        this.param = param
        return Pager(
            config = PagingConfig(
                pageSize = param.page.limit,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { source() }
        ).flow
    }

    override suspend fun getData(params: LoadParams<Int>): LoadResult<Int, UiVideo> {
        val currentOffset = params.key ?: param.page.offset
        val currentPage = Pageable(
            offset = currentOffset,
            limit = param.page.limit
        )
        val response = usecase(
            VideosUsecase.Parameter(
                author = param.author,
                page = currentPage
            )
        )
        return LoadResult.Page(
            data = response.items.map { it.mapToVideo { annotationUsecase(it) } },
            prevKey = if (currentOffset <= 0) null else currentOffset - 1,
            nextKey = if (response.items.isEmpty()) null else currentOffset + response.items.size
        )
    }

    data class Parameter(
        val author: String,
        val page: Pageable
    )
}