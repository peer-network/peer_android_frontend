package eu.peernetwork.blog.ui.usecase

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource.LoadParams
import androidx.paging.PagingSource.LoadResult
import eu.peernetwork.blog.domain.model.Filter.Criteria
import eu.peernetwork.blog.domain.model.Category
import eu.peernetwork.blog.domain.model.Content
import eu.peernetwork.blog.domain.usecase.AdvertUsecase
import eu.peernetwork.blog.domain.usecase.PostUsecase
import eu.peernetwork.blog.domain.usecase.PostUsecase.Companion.FEED
import eu.peernetwork.blog.ui.mapper.mapFromDomain
import eu.peernetwork.blog.ui.model.UiPost
import eu.peernetwork.core.common.paging.Pageable
import eu.peernetwork.core.common.provider.Dispatcher
import eu.peernetwork.core.ui.exception.NoContentException
import eu.peernetwork.core.ui.usecase.PagingUsecase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FeedUsecase @Inject constructor(
    private val usecase: PostUsecase,
    private val dispatcher: Dispatcher,
    private val advertUsecase: AdvertUsecase,
) : PagingUsecase<FeedUsecase.Parameter, UiPost>() {
    private lateinit var param: Parameter

    private var adsExhausted = false
    private var adsOffset = 0
    private var postOffset = 0

    override fun invoke(param: Parameter): Flow<PagingData<UiPost>> {
        this.param = param
        adsExhausted = false
        adsOffset = 0
        postOffset = 0
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

    override suspend fun getData(
        params: LoadParams<Int>
    ): LoadResult<Int, UiPost> = withContext(dispatcher.io) {
        val loadKey = params.key ?: 0
        if (!adsExhausted) {
            val ads = advertUsecase(
                AdvertUsecase.Parameter(
                    types = param.types,
                    category = param.category,
                    criteria = param.criteria,
                    page = Pageable(
                        offset = adsOffset,
                        limit = param.page.limit
                    )
                )
            )
            if (ads.items.isNotEmpty()) {
                adsOffset += ads.items.size
                val ui = ads.items.map {
                    it.mapFromDomain().copy(
                        pinnedBy = it.author.username
                    )
                }
                return@withContext LoadResult.Page(
                    data = ui,
                    prevKey = if (loadKey == 0) null else loadKey - ui.size,
                    nextKey = loadKey + ui.size
                )
            } else {
                adsExhausted = true
            }
        }
        val posts = usecase(
            PostUsecase.Parameter(
                types = param.types,
                category = param.category,
                criteria = param.criteria,
                page = Pageable(
                    offset = postOffset,
                    limit = param.page.limit
                )
            )
        )
        if (posts.items.isEmpty() && postOffset == 0) {
            return@withContext LoadResult.Error(NoContentException())
        }
        postOffset += posts.items.size
        val uiPosts = posts.items.map { it.mapFromDomain() }
        return@withContext LoadResult.Page(
            data = uiPosts,
            prevKey = if (loadKey == 0) null else loadKey - uiPosts.size,
            nextKey = if (uiPosts.isNotEmpty()) loadKey + uiPosts.size else null
        )
    }

    data class Parameter(
        val types: Set<Content.Type> = FEED,
        val category: Category = Category.NONE,
        val criteria: Criteria? = null,
        val page: Pageable
    )
}