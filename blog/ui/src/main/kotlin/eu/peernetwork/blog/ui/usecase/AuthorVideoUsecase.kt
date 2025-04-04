package eu.peernetwork.blog.ui.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import eu.peernetwork.blog.domain.model.Content
import eu.peernetwork.blog.domain.model.Filter
import eu.peernetwork.blog.domain.repository.ContentRepository
import eu.peernetwork.blog.ui.mapper.mapToVideo
import eu.peernetwork.blog.ui.model.UiVideo
import eu.peernetwork.core.common.model.Pageable
import eu.peernetwork.core.ui.usecase.PagingUsecase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AuthorVideoUsecase @Inject constructor(
    private val repository: ContentRepository
) : PagingUsecase<AuthorVideoUsecase.Parameter, UiVideo>() {
    private lateinit var param: Parameter

    override fun invoke(param: Parameter): Flow<PagingData<UiVideo>> {
        this.param = param
        return Pager(
            config = PagingConfig(
                pageSize = param.page.limit,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { this }
        ).flow
    }

    override suspend fun getData(params: LoadParams<Int>): LoadResult<Int, UiVideo> {
        val response = repository.getAll(
            filter = Filter(
                author = param.author,
                type = setOf(
                    Content.Type.VIDEO
                )
            ),
            param.page
        )
        return LoadResult.Page(
            data = response.items.map { it.mapToVideo() },
            prevKey = if (param.page.offset <= 0) null else param.page.offset - 1,
            nextKey = if (response.items.size < param.page.limit) {
                null
            } else {
                param.page.offset + 1
            }
        )
    }

    data class Parameter(
        val author: String,
        val page: Pageable
    )
}