package eu.peernetwork.blog.ui.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource.LoadParams
import androidx.paging.PagingSource.LoadResult
import eu.peernetwork.blog.domain.usecase.PhotosUsecase
import eu.peernetwork.blog.ui.mapper.mapToPhoto
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.core.ui.usecase.AnnotationUsecase
import eu.peernetwork.core.ui.usecase.PagingUsecase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserPostsUsecase @Inject constructor(
    private val usecase: PhotosUsecase,
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

    override suspend fun getData(params: LoadParams<Int>): LoadResult<Int, UiPost> {
        val currentOffset = params.key ?: param.page.offset
        val currentPage = Pageable(
            offset = currentOffset,
            limit = param.page.limit
        )
        val response = usecase(PhotosUsecase.Parameter(page = currentPage))
        return LoadResult.Page(
            data = response.items.map { it.mapToPhoto { annotationUsecase(it) } },
            prevKey = if (currentOffset <= 0) null else currentOffset - 1,
            nextKey = if (response.items.isNotEmpty()) {
                currentOffset + response.items.size
            } else {
                null
            }
        )
    }

    data class Parameter(val page: Pageable)
}